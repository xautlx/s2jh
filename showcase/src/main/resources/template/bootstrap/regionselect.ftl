<#include "/${parameters.templateDir}/bootstrap/controlheader.ftl" />
        <div class="input-append">
            <span class="add-on">省</span>
<@s.select list="parameters.provinceList" name="${parameters.provinceName?html}" 
value="parameters.provinceValue" id="${parameters.provinceId?html}" cssClass="${parameters.cssClass?html}"/>
<span class="add-on">地市</span>
<@s.select list="parameters.cityList" name="${parameters.cityName?html}" 
value="parameters.cityValue" id="${parameters.cityId?html}" cssClass="${parameters.cssClass?html}"/>
<span class="add-on">区县</span>
<#include "/${parameters.templateDir}/simple/select.ftl" />
        </div>
   <script type="text/javascript">
        $().ready(function() {
            $('#${parameters.cityId?html}').cascade('#${parameters.provinceId?html}', '${request.contextPath}/biz/sys/region-code!cityRegions');
            $('#${parameters.id?html}').cascade('#${parameters.cityId?html}', '${request.contextPath}/biz/sys/region-code!districtRegions');
        })
    </script>
<#include "/${parameters.templateDir}/bootstrap/controlfooter.ftl" />