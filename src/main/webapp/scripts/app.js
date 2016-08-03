function confirmDelete(obj) {
    var msg = "Are you sure you want to delete this " + obj + "?";
    ans = confirm(msg);
    return ans;
}

// 18n version of confirmDelete. Message must be already built.
function confirmMessage(obj) {
    var msg = "" + obj;
    ans = confirm(msg);
    return ans;
}

function checkAll() {
    if (document.getElementById('allChecker').checked) {
        $('.selectableCheckbox').prop('checked','checked');
    }
else {
        $('.selectableCheckbox').removeAttr('checked') ;
    }
}

// 03.08.2016 Хрипушин А.В. Не используется, подтверждения берутся из messages.properties
function getMessage(name) {
    jQuery.i18n.properties({
        name: 'js',
        mode:'both',
        path: 'scripts/'
    });
    var msg = jQuery.i18n.prop(name);
    return msg;
}
// Show the document's title on the status bar
window.defaultStatus=document.title;
