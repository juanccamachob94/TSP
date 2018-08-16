function generatePDF(elemento, nombre) {
    absoluto('.' + elemento);
    kendo.drawing.drawDOM($('.' + elemento)).then(function (group) {
        kendo.drawing.pdf.saveAs(group, nombre + '.pdf');
        relativo('.' + elemento);
    });
}

function tieneClase( elem, klass ) {
     return (" " + elem.className + " " ).indexOf( " "+klass+" " ) > -1;
}

var br = "<br/>";
$('.dropdown-button').dropdown({
    inDuration: 300,
    outDuration: 225,
    constrain_width: false, // Does not change width of dropdown to that of the activator
    hover: true, // Activate on hover
    gutter: 0, // Spacing from edge
    belowOrigin: false, // Displays dropdown below the button
    alignment: 'left' // Displays dropdown with edge aligned to the left of button
});
$('.dropdown-button').dropdown('open');
$('.dropdown-button').dropdown('close');

function mostrarModal(but, modal) {
    $('#' + modal).openModal();
    return false;
}

function cerrarModal(modal) {
    $('#' + modal).closeModal();
    return false;
}
function columna(m) {
    return '<div class="col m4">' + m + '</div>';
}

function escribir(c) {
    return c.split('Á').join('\u00C1').split('á').join('\u00E1').split('É').join('\u00C9').split('é').join('\u00E9').split('Í').join('\u00CD').split('í').join('\u00ED').split('Ó').join('\u00D3').split('ó').join('\u00F3').split('Ú').join('\u00DA').split('ú').join('\u00FA').split('Ñ').join('\u00D1').split('ñ').join('\u00F1').split('[').join('<strong>').split(']').join('</strong>');
}

function cerrarMensaje() {
    setTimeout(function () {
        $('.ui-icon-close').click();
    }, 3500);
}

function cargarPagina(pag) {
    window.location.href = pag + ".xhtml";
}

function cargarPagina(pag, time) {
    setTimeout(function () {
        window.location.href = pag + ".xhtml";
    }, time);
}

function cuadrarLabels() {
    Materialize.updateTextFields();
    $('select').material_select();
    $('.modal-trigger').leanModal();
    $('.collapsible').collapsible();
}
$(document).ready(function () {
    $('select').material_select();
    $('.modal-trigger').leanModal();

});
function desplegarMenuIzquierda() {
    estatico('#encabezado');
    //No responde a tiempo
    setTimeout(function(){
       $('.drag-target').width('100%'); 
    },100);
    var totalItemsIzq = 1;
    for(var i = 1; i <= totalItemsIzq; i++)
        if(tieneClase(document.getElementById('item_' + i.toString()),'active'))
            $('#item_' + i).click();
    $('#btnMenuIzquierdaMaterialize').click();
}
function efectuar(formValido, btn, form) {
    formValido[0] ? $('.' + btn).click() : $('#' + form + ' .errorCentrado .errorHTML').html(formValido[1]);
}

function contar(val, div, mx) {
    var len = val.value.length;
    len > mx ? val.value = val.value.substring(0, mx) : $('#cantidad_' + div).html('<div class="cantidad">caracteres restantes: ' + (mx - len) + '</div>');
}

$(function(){
    $('input[type="date"]').click(function(){
        if(navigator.userAgent.toLowerCase().indexOf('firefox') > -1) $('input[type="date"]').datepicker({dateFormat:'yy-mm-dd'});
    });
});

//==============================================================================================

function iniciarSesion() {
    $('.btnIniciarSesion').click();
}

function cerrarSesion() {
    $('.btnCerrarSesion').click();
}

function r_mostrarModalRegistrar() {
    $('.linkModalRegistrar').click();
}

function r_mostrarModalProyectosUsuarioSesion() {
    $('.linkModalProyectosUsuarioSesion').click();
}

function r_mostrarModalInterrupcion() {
    $('.linkModalInterrupcionPrevista').click();
}

function r_mostrarModalInterrupcion() {
    $('.linkModalInterrupcionNoPrevista').click();
}

function formPEERValido() {
    var m = "";
    var totalTrabajo = $('.peer_totalTrabajo').val();
    var totalDificultad = $('.peer_totalDificultad').val();
    if (parseInt(totalTrabajo) != 100) {
        m += columna("El [total trabajo] debe ser igual al 100%");
    }
    if (parseInt(totalDificultad) != 100) {
        m += columna("El [total dificultad] debe ser igual al 100%");
    }
    if (m == "")
        return [1];
    return [0, '<div class="row">' + escribir(m) + '</div>'];
}

function formPIPValido() {
    var m = "";
    var parte = $('.pip_parte').val();
    var prioridad = $('.pip_prioridad').val();
    var problema = $('.pip_problema').val();
    var propuesta = $('.pip_propuesta').val();
    if (parte == "")
        m += columna("La [parte] es requerida");
    if (prioridad == "")
        m += columna("La [prioridad] es requerida");
    if (problema == "")
        m += columna("El [problema] es requerido");
    if (propuesta == "")
        m += columna("La [propuesta] es requerida");
    if (m == "")
        return [1];
    return [0, '<div class="row">' + escribir(m) + '</div>'];
}


function formGuardarCCRValido() {
    var m = "";
    var parte = $('ccr_parte').val();
    var razonCambio = $('ccr_razonCambio').val();
    var beneficiosCambio = $('ccr_beneficiosCambio').val();
    var impactoCambio = $('ccr_impactoCambio').val();
    var infCambio = $('ccr_infCambio').val();
    if (parte == "")
        m += columna("La [parte] es requerida");
    if (razonCambio == "")
        m += columna("La [razón del cambio] es requerida");
    if (beneficiosCambio == "")
        m += columna("Los [beneficios del cambio] son requeridos");
    if (impactoCambio == "")
        m += columna("El [impacto del cambio] es requerido");
    if (infCambio == "")
        m += columna("La [información del cambio] es requerida");
    if (m == "")
        return [1];
    return [0,'<div class="row">' + escribir(m) + '</div>'];
}


function formRegistrarValido() {
    var m = "";
    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    var identificacion = $('.registro_identificacion').val();
    var nombre = $('.registro_nombre').val();
    var apellido = $('.registro_apellido').val();
    var telefono = $('.registro_telefono').val();
    var email = $('.registro_email').val();
    var clave = $('.registro_clave').val();
    var confirmaClave = $('.registro_confirmaClave').val();

    if (identificacion == "")
        m += columna("La [identificación] es requerida");
    if (nombre == "")
        m += columna("El [nombre] es requerido");
    if (apellido == "")
        m += columna("El [apellido] es requerido");
    if (telefono == "")
        m += columna("El [teléfono] es requerido");
    if (email == "")
        m += columna("El [email] es requerido");
    else if (!re.test(email))
        m += columna("El [email] no es válido");
    if (clave == "")
        m += columna("La [clave] es requerida");
    if (confirmaClave == "")
        m += columna("La [confirmación] es requerida");
    if (clave != confirmaClave)
        m += columna("Las claves no coinciden");

    if (m == "")
        return [1];
    return [0, '<div class="row">' + escribir(m) + '</div>'];
}

function r_mostrarModalNuevoProducto() {
    $('.linkModalNuevoProducto').click();
}
function r_cerrarModalNuevoProducto() {
    cerrarModal('modalNuevoProducto');
}

function r_mostrarModalNuevaParte() {
    $('.linkModalNuevaParte').click();
}

function r_cerrarModalNuevaParte() {
    cerrarModal('modalNuevaParte');
}

function r_cerrarModalPararActividad() {
    cerrarModal('modalPararActividad');
}

function r_cerrarModalInterrPrev() {
    cerrarModal('modalInterrupcionPrevista');
}

function r_cerrarModalInterrNoPrev() {
    cerrarModal('modalInterrupcionImprevista');
}

function r_mostrarModalInterrnPrev() {
    $('.linkModalInterrupcionPrevista').click();
}

function r_mostrarModalInterrupcionNoPrevista() {
    $('.linkModalInterrupcionNoPrevista').click();
}

function r_mostrarModalSeleccionCarpetaArchivo() {
    $('.linkModalSeleccionCarpetaArchivo').click();
}

function r_limpiarConfirmaClave() {
    $('.registro_clave').val("");
    $('.registro_confirmaClave').val("");
}

function r_registrar() {
    cerrarModal('modalRegistrar');
}



function formAsignacionRolesValido() {
    return [1];
}

function r_mostrarModalPararActividad() {
    $('.linkModalPararActividad').click();
}

function mostrarModalNuevoRegistroLOGD(){
    $('.linkModalNuevoRegistroLOGD').click();
}
function r_cerrarModalNuevoRegistroLOGD() {
    cerrarModal('modalNuevoRegistroLOGD');
}

function mostrarModalNuevoRegistroItl(){
    $('.linkModalNuevoRegistroItl').click();
}
function r_cerrarModalNuevoRegistroItl() {
    cerrarModal('modalNuevoRegistroItl');
}
function mostrarModalAgregarMetaProyecto(){
    $('.linkModalCargarMetaProyectoTSP').click();
}

function r_cerrarModalAgregarMetaProyecto(){
    cerrarModal('modalSeleccionMetasTSPProyecto');
}

function mostrarModalNuevaMetaProyecto(){
    $('.linkModalNuevaMetaProyecto').click();
}

function r_cerrarModalNuevaMetaProyecto(){
    cerrarModal('modalNuevaMetaProyecto');
}

function mostrarModalAgregarMetaRol(indice){
    $('.linkModalCargarMetaRolTSP' + indice).click();
}

function mostrarModalNuevaMetaRol(indice){
    $('.linkModalNuevaMetaRol' + indice).click();
}

function r_cerrarModalAgregarMetaRol(indice){
    cerrarModal('modalSeleccionMetasTSPRol' + indice);
}

function r_cerrarModalNuevaMetaRol(indice){
    cerrarModal('modalNuevaMetaRol' + indice);
}

function mostrarModalSigFase(){
    $('.linkModalSiguienteFaseOCiclo').click();
}

function r_cerrarModalSiguienteFaseCiclo(){
    cerrarModal('modalSiguienteFase' + indice);
}

function mostrarModalCambioClave(){
    $('.linkModalCambioClave').click();
}

function r_cerrarModalCambioClave(){
    cerrarModal('modalCambioClave');
}

function abrirModalInterrupcionPrevista(){
    $('#modalInterrupcionPrevista').openModal({complete: function () {$('.btnCancelarInterrupcionPrevista').click();}});
}

function abrirModalInterrupcionNoPrevista(){
    $('#modalInterrupcionImprevista').openModal({complete: function () {$('.btnCancelarInterrupcionNoPrevista').click();}});
}

function abrirModalGrafica(n){
    $('#modalGrafica').openModal();
    $('.linkSolicitudGrafica' + n).click();
}
