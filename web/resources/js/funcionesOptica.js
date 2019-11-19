/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
window.onload = function () {
    nobackbutton();
};

function nobackbutton() {
    window.location.hash = "no-back-button";
    window.location.hash = "Again-No-back-button"; //chrome
    window.onhashchange = function () {
        window.location.hash = "no-back-button";
    };
}

$(document).ready(function () {
    $("#tableCli table tbody tr").click(function () {
        var valor = $(this).find("td").eq(0).html();
        console.log(valor);
//                    $("#valHora").val(valor);
        $("#tableCli div.row div.col-md-4 input.input-perfil").val(valor);
    });
//    if ($("#div-citas") !== null) {
//        document.getElementById('div-citas').scrollIntoView();
//    }
//    $("#div-citas").scroll();
});