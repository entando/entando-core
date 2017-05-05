function generateCodeFromTitle(titleId, codeId)
{

        if (titleId.substring(0,1) != "#"){
            titleId="#"+titleId; 
        }
        if (codeId.substring(0,1) != "#"){
            codeId="#"+codeId; 
        }        

        $(titleId).keyup(function() {                                     
            var titleVal = $(titleId).val().replace(/[^\x00-\x7F]/g, "");             
            var codeVal=titleVal.toLowerCase().trim().replace(/["~!@#$%^&*\(\)+=`{}\[\]\|\\:;'<>,.\/?"\- \t\r\n]+/g, '_');                                    
            $(codeId).val(codeVal);                    
        });

};            