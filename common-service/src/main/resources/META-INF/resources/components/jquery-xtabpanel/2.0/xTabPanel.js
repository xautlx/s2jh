/**
 * TabPanel Javascript Component v2.0
 * 
 * Copyright 2010, Marker King Dual licensed under the MIT or GPL Version 2
 * licenses.
 * 
 * Build on jQuery JavaScript Library v1.4.4
 * 
 * Date: 2010.12.22 $ protected _ private
 * 
 * @param {Object}
 *            config
 * 
 * {string | jQuery} config.renderTo {string | number} config.width {string |
 * number} config.height {Array} config.items {boolean} config.border {string |
 * number} config.defaultTab {number} config.maxTab {number} config.moveSize
 * {boolean} config.autoResize
 */
var TabPanel = (function() {
    return function(config) {
        this.id = 'jTabPanel-' + config.id;
        this.renderTo = config.renderTo || $(document.body);
        this.render = typeof this.renderTo === 'string' ? $('#' + this.renderTo) : this.renderTo;
        this.width = config.width || '100%';
        this.height = config.height || '100%';
        this.items = config.items;
        this.border = config.border;
        this.defaultTab = config.defaultTab;
        this.maxTab = config.maxTab;
        this.moveSize = config.moveSize || 100;
        this.autoResize = config.autoResize;
        this.icon = config.icon;
        // 私有属性
        this.$tabs = {};
        this.$tabsArray = [];
        this.$maxMove = 0;
        this.$scrolled = false;
        this.$scrollFinished = true;
        this._init();
    }
})();

TabPanel.prototype = {
    /**
     * 初始化
     */
    _init : function() {
        var $tabEntity = this;

        if (this.autoResize) {
            this.width = '100%';
            this.height = '100%';
            // IE会触发两次resize，FF3中有所见即所得的拖动，一直在触发resize，所以用Timer来控制
            var _resizeTimer;
            $(window).bind('resize.tabpanel', function() {
                window.clearTimeout(_resizeTimer);
                _resizeTimer = window.setTimeout(function() {
                    $tabEntity.resize();
                }, 300);
            });
        }
        // 组件HTML
        var _tabHTML = [];
        _tabHTML.push('<div class="jTabPanel" id="' + this.id + '">');// TabPanel
        // start
        _tabHTML.push('<div class="tab-contrl-wrap">');// tab contrl wrap start
        _tabHTML.push('<div class="tab-scroll tab-left-button">&lt;</div>');// tab
                                                                            // left
                                                                            // button
        _tabHTML.push('<div class="tab-scroll tab-right-button">&gt;</div>');// tab
                                                                                // right
                                                                                // button
        _tabHTML.push('<div class="tab-scroll tab-refresh-button show"><i class="icon-refresh icon-white" style="cursor:pointer"></i></div>');// tab
                                                                                                                                                // right
                                                                                                                                                // button
        _tabHTML.push('<div class="tab-item-wrap">');// tab item wrap start
        _tabHTML.push('<ul class="tab-item-move"></ul>');
        _tabHTML.push('</div>');// tab item wrap end
        _tabHTML.push('<div class="panel-spacer"></div>');// space line
        _tabHTML.push('</div>');// tab contrl wrap end
        _tabHTML.push('<div class="panel-content"></div>');// HTML content
        _tabHTML.push('</div>');// TabPanel end
        this.render.append(_tabHTML.join(''));

        // 获得DOM
        this.jTabPanel = $('#' + this.id);
        this.tabContrlWrap = $('#' + this.id + ' .tab-contrl-wrap');
        this.tabItemWrap = $('#' + this.id + ' .tab-item-wrap');
        this.tabItemMove = $('#' + this.id + ' .tab-item-move');
        this.tabContent = $('#' + this.id + ' .panel-content');
        this.leftButton = $('#' + this.id + ' .tab-left-button').select(function() {
            return false;
        });
        this.rightButton = $('#' + this.id + ' .tab-right-button').select(function() {
            return false;
        });
        this.refreshButton = $('#' + this.id + ' .tab-refresh-button').select(function() {
            return false;
        });

        var _timer;
        // 左右滚动事件
        this.leftButton.bind({
            click : function() {
                window.clearTimeout(_timer);
                _timer = window.setTimeout(function() {
                    $tabEntity._move('+', $tabEntity.moveSize);
                }, 200);
            },
            dblclick : function() {
                window.clearTimeout(_timer);
                $tabEntity._move('+', Math.abs(parseInt($tabEntity.tabItemMove.css('margin-left'), 10)));
            }
        });
        this.rightButton.bind({
            click : function() {
                window.clearTimeout(_timer);
                _timer = window.setTimeout(function() {
                    $tabEntity._move('-', $tabEntity.moveSize);
                }, 200);
            },
            dblclick : function() {
                window.clearTimeout(_timer);
                $tabEntity._move('-', $tabEntity.$maxMove + parseInt($tabEntity.tabItemMove.css('margin-left'), 10));
            }
        });

        // 刷新活动标签事件
        this.refreshButton.bind({
            click : function() {
                // 获得当前激活卡
                var _activeTab = $tabEntity.$tabs[$tabEntity.active];
                $tabEntity.refresh($(_activeTab).attr("id"));
            }
        });

        // 判断是否有border
        if (this.border) {
            this.jTabPanel.addClass('jTabPanelBorder');
        }
        this.update();
        // 循环添加item
        for ( var i = 0; i < this.items.length; i++) {
            this.addTab(this.items[i]);
        }
        // 打开默认选项卡
        if (this.defaultTab != undefined) {
            switch (typeof this.defaultTab) {
            case 'string':
                this.show(this.defaultTab);
                break;
            case 'number':
                this.show(this._getItemObject(this.defaultTab).id);
                break;
            }
        }
    },
    /**
     * 更新
     */
    update : function() {
        // 计算border宽度和高度
        this.border_w = this.jTabPanel.outerWidth() - this.jTabPanel.width();
        this.border_h = this.jTabPanel.outerHeight() - this.jTabPanel.height();
        // 设置整体高度和宽度，需要两次设置，防止百分比报错
        this.jTabPanel.css({
            width : this.width,
            height : this.height
        }).css({
            width : this.jTabPanel.width() - this.border_w,
            height : this.jTabPanel.height() - this.border_h
        });
        // 设置内容高度
        this.tabContent.height(this.jTabPanel.outerHeight() - this.tabContrlWrap.outerHeight() - this.border_h);
        this._updateWhere();
        for ( var i = 0; i < window.frames.length; i++) {
            window.frames[i].$.resetCalculateGridWidth();
        }
    },
    /**
     * 添加选项卡
     * 
     * @param {Object}
     *            item
     * 
     * {string} item.id {string} item.title {number} item.width {string}
     * item.icon {boolean} item.closable {boolean} item.lazyload
     */
    addTab : function(item) {
        var $tabEntity = this;
        if (!this.$scrollFinished) {
            return;
        }
        // 判断是否已有tab
        if (this.$tabs[item.id]) {
            this.show(item.id);
            return;
        } else if (this.maxTab <= this.$tabsArray.length) {
            alert('超出最大个数，不能打开');
            return;
        }
        // 组件HTML
        var _itemHTML = [];
        _itemHTML.push('<li id="' + item.id + '">');
        if (item.position) {
            item.position.a = item.position.a || '';
            item.position.b = item.position.b || item.position.a;
            _itemHTML.push('<div class="tab-item-title-icon" style="background-image: url(' + this.icon + ');"></div>');
            _itemHTML.push('<div class="tab-item-title tab-item-title-nopadding">');
        } else {
            item.position = {};
            item.position.a = item.position.b = '';
            _itemHTML.push('<div class="tab-item-title">');
        }
        _itemHTML.push(item.title);
        _itemHTML.push('</div>');
        _itemHTML.push('</li>');
        this.tabItemMove.append(_itemHTML.join(''));

        item.closable = item.closable || false;
        item.content = $('<div class="content-wrap"></div>');
        item.tabTitle = $('#' + item.id);
        item.tabTitleText = $('.tab-item-title', item.tabTitle);
        item.tabTitleIcon = $('.tab-item-title-icon', item.tabTitle);
        item.tabTitleIcon.css('background-position', item.position.a);
        this.tabContent.append(item.content);

        // 绑定事件
        item.tabTitle.click(function() {
            $tabEntity.show(item.id);
            for ( var i = 0; i < window.frames.length; i++) {
                window.frames[i].$.resetCalculateGridWidth();
            }
        })
        this._updateTabItem(item);
        // 设置前置卡
        item.pretab = this.active;
        // 放入数组中
        this.$tabsArray.push({
            id : item.id
        });
        // 将item写入对象中
        this.$tabs[item.id] = item;
        // 更新
        this._updateWhere();
        this._showScroll();
        // 显示
        if (!item.lazyload) {
            this.show(item.id);
        }
    },
    /**
     * 更新卡，方便使用
     * 
     * @param {Object}
     *            item 同上Item
     */
    _updateTabItem : function(item) {
        var $tabEntity = this;
        // 重置宽度
        item.tabTitleText.width('');
        // 补丁宽度
        var _fixWidth = 0;
        // 恢复未绑定状态
        item.tabTitle.unbind('dblclick');
        // 可关闭，增加补丁宽度，绑定关闭事件
        if (item.closable) {
            // 已有关闭按钮的话，删除点击事件
            if (item.closer) {
                item.closer.unbind('click');
            } else { // 添加按钮
                item.closer = $('<div class="tab-closer"></div>');
                item.tabTitle.append(item.closer);
            }
            // 计算补丁宽度
            _fixWidth = item.closer.outerWidth(true) + parseInt(item.closer.css('right'), 10);
            // 绑定事件
            item.closer.click(function() {
                $tabEntity.removeTab(item.id);
            });
            item.tabTitle.dblclick(function() {
                $tabEntity.removeTab(item.id);
            });
        } else { // 如果有关闭按钮，删除关闭按钮
            if (item.closer) {
                item.closer.remove();
                delete item['closer'];
            }
        }
        // 计算icon补丁宽度
        if (item.position) {
            _fixWidth += item.tabTitleIcon.width();
        }
        // 计算title的宽度差值
        var _subWidth = item.tabTitleText.outerWidth(true) - item.tabTitleText.width();
        // 如果设置了宽度
        if (item.width) {
            // 先给LI设置宽度
            item.tabTitle.width(item.width);
            // 判断是否超出宽度
            if (item.tabTitleText.outerWidth(true) > (item.width - _fixWidth)) {
                // 增加...补丁
                var _dot = $('<div class="fix">...</div>');
                item.tabTitleText.after(_dot);
                // 设置文字层宽度（总宽度-补丁宽度-文字层宽度差值）
                _fixWidth += _dot.outerWidth(true);
                item.tabTitleText.width(item.width - _fixWidth - _subWidth);
                // 设置title
                item.tabTitle.attr('title', item.title);
            } else { // 移除...补丁
                item.tabTitle.children('.fix').remove();
                item.tabTitle.removeAttr('title');
            }
        } else {
            item.tabTitle.width(item.tabTitleText.outerWidth(true) + _fixWidth);
        }
    },
    /**
     * 显示选项卡
     * 
     * @param {string}
     *            tabId
     */
    show : function(tabId) {
        tabId = this._getItemId(tabId);
        // 是否已经显示
        if (this.active === tabId) {
            return;
        }
        // 判断是否有该ID的卡
        if (this.$tabs[tabId]) {
            // 隐藏active，显示本卡
            if (this.$tabs[this.active]) {
                this.$tabs[this.active].tabTitle.removeClass('active');
                this.$tabs[this.active].content.css('display', 'none');
                this.$tabs[this.active].tabTitleIcon.css('background-position', this.$tabs[this.active].position.a);
            }
            this.$tabs[tabId].tabTitle.addClass('active');
            this.$tabs[tabId].content.css('display', 'block');
            this.$tabs[tabId].tabTitleIcon.css('background-position', this.$tabs[tabId].position.b);
            this.active = tabId;
            // 延迟加载判断是否已加载
            if (this.$tabs[tabId].content.html() === '') {
                this.$tabs[tabId].content.html((this.$tabs[tabId].htmlObject = $(this.$tabs[tabId].html)));
                if ($.browser.msie && $.browser.version === '6.0') {
                    this.$tabs[tabId].htmlObject.attr('src', this.$tabs[tabId].htmlObject.attr('src'));
                }
            }
            
            var mid=tabId.substring(9, tabId.length);
            //alert("mid=" + mid);
            $.address.value(mid);
        } else {
            alert('ID not found.');
        }
        this.moveToVisible();
    },
    /**
     * 关闭选项卡
     * 
     * @param {string}
     *            tabId
     */
    removeTab : function(tabId) {
        tabId = this._getItemId(tabId);
        var _pretab = this.$tabs[tabId].pretab;
        // 从DOM中删除
        this.$tabs[tabId].tabTitle.empty();
        this.$tabs[tabId].tabTitle.remove();
        this.$tabs[tabId].content.empty();
        this.$tabs[tabId].content.remove();
        // 删除属性
        delete this.$tabs[tabId];
        for ( var i = 0; i < this.$tabsArray.length; i++) {
            if (this.$tabsArray[i].id === tabId) {
                this.$tabsArray.splice(i, 1);
                this.update();
                this._showScroll();
                break;
            }
        }
        // 如果被关闭卡就是当前卡
        if (this.active === tabId) {
            // 显示前置卡
            if (_pretab && this.$tabs[_pretab]) {
                this.show(_pretab);
            } else if (this.$tabsArray.length > 0) {
                this.show(this._getItemObject(0).id);
            }
        } else { // 移动到可见
            this.moveToVisible();
        }
        this._updateWhere();
        this._showScroll();
    },
    /**
     * 设置每个卡所在位置，计算最大移动量
     */
    _updateWhere : function() {
        this.$maxMove = 0;
        for ( var i = 0; i < this.$tabsArray.length; i++) {
            this.$maxMove += this.$tabs[this.$tabsArray[i].id].tabTitle.outerWidth(true);
            this.$tabsArray[i].where = this.$maxMove;
        }
        if (this.$scrolled) {
            // 减去左右margin的宽度
            var _lm = this.leftButton.outerWidth(true);
            var _rm = this.rightButton.outerWidth(true) + parseInt(this.tabItemWrap.css('padding-right'), 10);
            this.tabItemWrap.width(this.tabContrlWrap.width() - _lm - _rm);
        } else {
            this.tabItemWrap.width(this.tabContrlWrap.width());
        }
        this.$maxMove -= this.tabItemWrap.width();
    },
    /**
     * 显示滚动条
     */
    _showScroll : function() {
        // 获得最后一个选项卡所在位置
        var _liWhere = this.$tabsArray[this.$tabsArray.length - 1].where;
        // 获得控制层的宽度
        var _contrlWidth = this.tabContrlWrap.width();
        // 超出控制层，并且未显示滚动条
        if (_liWhere > _contrlWidth && !this.$scrolled) {
            this.tabItemWrap.addClass('tab-item-wrap-scroll');
            this.leftButton.addClass('show');
            this.rightButton.addClass('show');
            this.$scrolled = true;
            this._updateWhere();
        } else if (_contrlWidth > _liWhere && this.$scrolled) { // 为超出控制层，并且已显示滚动条
            this.tabItemWrap.removeClass('tab-item-wrap-scroll');
            this.leftButton.removeClass('show');
            this.rightButton.removeClass('show');
            this.$scrolled = false;
            this._updateWhere();
        }
    },
    /**
     * 判断滚动条左右是否可用
     */
    _useableScroll : function() {
        this.$scrollFinished = !this.$scrollFinished;
        if (this.$scrolled) {
            var _itemWrapWhere = parseInt(this.tabItemMove.css('margin-left'), 10);
            // 左不能
            if (_itemWrapWhere >= 0) {
                this.leftButton.attr('disabled', true).addClass('tab-button-disabled');
                this.rightButton.removeAttr('disabled').removeClass('tab-button-disabled');
            } else if (Math.abs(_itemWrapWhere) >= this.$maxMove) { // 右不能
                this.leftButton.removeAttr('disabled').removeClass('tab-button-disabled');
                this.rightButton.attr('disabled', true).addClass('tab-button-disabled');
            } else { // 全能
                this.leftButton.removeAttr('disabled').removeClass('tab-button-disabled');
                this.rightButton.removeAttr('disabled').removeClass('tab-button-disabled');
            }
        }
    },
    /**
     * 移动
     * 
     * @param {string}
     *            operator +|-
     * @param {number}
     *            m
     */
    _move : function(operator, m) {
        var $tabEntity = this;
        if (!this.$scrollFinished) {
            return;
        }
        // 获得当前位置
        var _nowWhere = parseInt(this.tabItemMove.css('margin-left'), 10);
        // 判断+或-
        if (operator === '+') {
            // 为0直接返回
            if (_nowWhere === 0) {
                return;
            } else if ((_nowWhere + m) > 0) { // 加上移动位置后若大于0
                // 移动量为绝对值
                m = Math.abs(_nowWhere);
            }
        } else if (operator === '-') {
            // 当前位置绝对值+移动量，若大于最大移动量
            if ((Math.abs(_nowWhere) + m) > this.$maxMove) {
                // 移动量为差值（_nowWhere为负数）
                m = this.$maxMove + _nowWhere;
            }
        }
        // 移动量大于0时才执行移动
        if (m > 0) {
            this.$scrollFinished = !this.$scrollFinished;
            this.tabItemMove.animate({
                'margin-left' : operator + '=' + m
            }, 300, function() {
                $tabEntity._useableScroll();
            });
        }
    },

    /**
     * 直接移动到合适的可见位置
     */
    moveToVisible : function() {
        // 获得move层当前的margin
        var _movePosition = parseInt(this.tabItemMove.css('margin-left'), 10);
        // 获得当前激活卡
        var _activeTab = this.$tabs[this.active];
        // 获得当前激活卡的属性
        var _activeTabProperty = this._getItemProperty(this.active);
        // 获得卡左侧位置
        var _activeTabLeftPosition = _activeTabProperty.where - _activeTab.tabTitle.outerWidth(true) + _movePosition;
        // 获得卡右侧位置
        var _activeTabRightPosition = _activeTabProperty.where + _movePosition;
        // 获得卡包围层的宽度
        var _itemWrapWidth = this.tabItemWrap.width();
        // 已显示滚动条
        if (this.$scrolled) {
            // 如果左侧在范围外
            if (_activeTabLeftPosition < 0) {
                // 如果最后一个卡的右侧位置+移动后的位置还未到达最右端
                if (this.$tabsArray[this.$tabsArray.length - 1].where + _movePosition + Math.abs(_activeTabLeftPosition) < _itemWrapWidth) {
                    this._move('+', _itemWrapWidth - (this.$tabsArray[$tabsArray.length - 1].where + _movePosition));
                } else { // 移动到可见位置
                    this._move('+', Math.abs(_activeTabLeftPosition));
                }
            } else if (_activeTabRightPosition > _itemWrapWidth) { // 如果右侧在范围外
                this._move('-', _activeTabRightPosition - _itemWrapWidth);
            } else if (this.$tabsArray[this.$tabsArray.length - 1].where + _movePosition < _itemWrapWidth) { // 左右都在范围内，但最后一个卡右侧未达到最右端
                this._move('+', _itemWrapWidth - (this.$tabsArray[this.$tabsArray.length - 1].where + _movePosition));
            }
        } else {
            // 如果左侧在范围外
            if (_movePosition < 0) {
                // 移动到最左端
                this._move('+', Math.abs(_movePosition));
            }
        }
    },
    /**
     * 获得ItemID
     * 
     * @param {string |
     *            number} tabId
     */
    _getItemId : function(tabId) {
        if (typeof tabId === 'number') {
            tabId = this._getItemObject(tabId).id;
        }
        return tabId;
    },
    /**
     * 获得Item的属性
     * 
     * @param {string}
     *            tabId
     */
    _getItemProperty : function(tabId) {
        for ( var i = 0; i < this.$tabsArray.length; i++) {
            if (this.$tabsArray[i].id === tabId) {
                return this.$tabsArray[i];
            }
        }
    },
    /**
     * 获得Item对象
     * 
     * @param {number}
     *            index
     */
    _getItemObject : function(index) {
        return this.$tabs[this.$tabsArray[index].id];
    },
    /**
     * 获得卡Title
     * 
     * @param {string |
     *            number} tabId
     */
    getTitle : function(tabId) {
        tabId = this._getItemId(tabId);
        return this.$tabs[tabId].tabTitle.children('.tab-item-title').text();
    },
    /**
     * 设置卡Title
     * 
     * @param {string |
     *            number} tabId
     * @param {string}
     *            title
     */
    setTitle : function(tabId, title) {
        tabId = this._getItemId(tabId);
        if (this.$tabs[tabId].title === title) {
            return;
        }
        this.$tabs[tabId].title = title;
        this.$tabs[tabId].tabTitle.children('.tab-item-title').text(title);
        this._updateTabItem(this.$tabs[tabId]);
        this._updateWhere();
        this.moveToVisible();
    },
    /**
     * 获得卡宽度
     * 
     * @param {string |
     *            number} tabId
     */
    getWidth : function(tabId) {
        tabId = this._getItemId(tabId);
        return this.$tabs[tabId].width;
    },
    /**
     * 设置卡宽度
     * 
     * @param {string |
     *            number} tabId
     * @param {string |
     *            number} width
     */
    setWidth : function(tabId, width) {
        tabId = this._getItemId(tabId);
        if (this.$tabs[tabId].width === width) {
            return;
        }
        this.$tabs[tabId].width = width;
        this._updateTabItem(this.$tabs[tabId]);
        this._updateWhere();
        this.moveToVisible();
    },
    /**
     * 获得卡是否可关闭
     * 
     * @param {string |
     *            number} tabId
     */
    getClosable : function(tabId) {
        tabId = this._getItemId(tabId);
        return this.$tabs[tabId].closable;
    },
    /**
     * 设置卡是否可关闭
     * 
     * @param {string |
     *            number} tabId
     * @param {boolean}
     *            closable
     */
    setClosable : function(tabId, closable) {
        tabId = this._getItemId(tabId);
        if (this.$tabs[tabId].closable === closable) {
            return;
        }
        this.$tabs[tabId].closable = closable;
        this._updateTabItem(this.$tabs[tabId]);
        this._updateWhere();
        this.moveToVisible();
    },
    /**
     * 重置高度宽度，可传null
     * 
     * @param {number}
     *            width
     * @param {number}
     *            height
     */
    resize : function(width, height) {
        this.width = width || this.width;
        this.height = height || this.height;
        this.update();
        this._showScroll();
        this.moveToVisible();
    },

    refresh : function(tabId) {
        // 得到下标
        tabId = this._getItemId(tabId);
        // 如果没有该选项卡,则不执行刷新
        if (!this.$tabs[tabId]) {
            return false;
        } else {
            var url = window.frames[tabId + 'Frame'].location.href;
            url = AddOrReplaceUrlParameter(url, "_", new Date().getTime());
            window.frames[tabId + 'Frame'].location.href = url;
        }
    },

    /**
     * 刷新选项卡
     * 
     * @param {string |
     *            number} tabId
     */
    flush : function(tabId) {
        // 得到下标
        tabId = this._getItemId(tabId);
        // 如果没有该选项卡,则不执行刷新
        if (!this.$tabs[tabId]) {
            return false;
        } else {
            var iframes = this.$tabs[tabId].content.find('iframe');
            if (iframes.length > 0) {
                var frameId = this.$tabs[tabId].id + 'Frame';
                var iframeObj = window.frames[frameId];
                // (基本废弃)this.iterateFlush(window.frames[frameId]);
                if (iframeObj.window.frames.length > 0) {
                    for ( var i = 0; i < iframeObj.window.frames.length; i++) {
                        var childFrame = iframeObj.window.frames[i];
                        // 将frame中的所有form提交
                        if (childFrame.document.forms.length > 0) {
                            for ( var j = 0; j < childFrame.document.forms.length; j++) {
                                // form提交时遇到异常,则将该页面刷新
                                try {
                                    childFrame.document.forms[j].submit();
                                } catch (e) {
                                    childFrame.location.reload();
                                }
                            }
                        } else { // 没有form,直接刷新
                            childFrame.location.reload();
                        }
                    }
                } else {
                    if (iframeObj.document.forms.length > 0) {
                        for ( var j = 0; j < iframeObj.document.forms.length; j++) {
                            // form提交时遇到异常,则将该页面刷新
                            try {
                                iframeObj.document.forms[j].submit();
                            } catch (e) {
                                iframeObj.location.reload();
                            }
                        }
                    } else { // 没有form,直接刷新
                        iframeObj.location.reload();
                    }
                }
            }
        }
    },
    /**
     * 递归刷新(基本废弃)
     * 
     * @param {object}
     *            iframeObj
     */
    iterateFlush : function(iframeObj) {
        /** 必须使用frames才能得到相应对象 */
        // 如果当前frame中有多个frame,则再次递归刷新
        if (iframeObj.window.frames.length > 0) {
            for ( var i = 0; i < iframeObj.window.frames.length; i++) {
                this.iterateFlush(iframeObj.window.frames[i]);
            }
        } else {
            // 将frame中的所有form提交
            if (iframeObj.document.forms.length > 0) {
                for ( var i = 0; i < iframeObj.document.forms.length; i++) {
                    // form提交时遇到异常,则将该页面刷新
                    try {
                        iframeObj.document.forms[i].submit();
                    } catch (e) {
                        iframeObj.location.reload();
                    }
                }
            } else { // 没有form,直接刷新
                iframeObj.location.reload();
            }
        }
    }
}