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

// "Выделить всё"
function checkAll() {
    if (document.getElementById('allChecker').checked) {
        $('.selectableCheckbox').prop('checked',true);
    }
else {
        $('.selectableCheckbox').prop('checked',false) ;
    }
}

// Обработчик включения чекбоксов
function checkboxProcessing() {
    var checkedCount = 0;
    var checkboxCount = $('.selectableCheckbox').length;
    $('.selectableCheckbox').each(function () {
        if ($(this).prop('checked')) {
            checkedCount++;
        }
    });
    if(checkedCount > 0) {
        $('#buttonDelete').prop('disabled',false);
        $('#buttonActivate').prop('disabled',false);
        $('#buttonDeactivate').prop('disabled',false);
        $('#buttonExport').prop('disabled',false);
        if (checkedCount == checkboxCount) {
            $('#allChecker').prop('checked',true)
        }
    }
    else {
        $('#buttonDelete').prop('disabled',true);
        $('#buttonActivate').prop('disabled',true);
        $('#buttonDeactivate').prop('disabled',true);
        $('#buttonExport').prop('disabled',true);
        $('#allChecker').prop('checked',false)
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
