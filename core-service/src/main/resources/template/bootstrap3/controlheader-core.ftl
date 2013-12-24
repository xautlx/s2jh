<#--
/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
-->
<#if parameters.label??>
<div class="control-group">
  <label class="control-label"
<#if parameters.id??>
        for="${parameters.id?html}" <#t/>
</#if> 
<#if parameters.tooltip??>
        title="${parameters.tooltip?html}" <#t/>      
</#if>  
    ><#t/>
<#if parameters.required?default(false)>
        <span class="required">*</span><#t/>
</#if>
<#if parameters.tooltip??>
        <i class="icon-info-sign"></i>
</#if> 
     ${parameters.label?html}${parameters.labelseparator!""?html}  
    </label><#t/>
<#if parameters.tooltip??>
<script type="text/javascript">
    $(function() {
        $("label[title]").tooltip({
            placement : 'right',
            html : true
        });
    })
</script>
</#if>
    <div class="controls">
</#if>