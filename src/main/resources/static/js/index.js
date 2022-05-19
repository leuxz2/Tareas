'use strict'

// -------------------------- Acciones a ejecutar cuando se carga el documento HTML---------------------------

document.addEventListener('DOMContentLoaded', function () {
    let contenedorComoUsar = document.getElementById('contenedorComoUsar');
    contenedorComoUsar.classList.add('invisible');

    document.getElementById('btnComoUsar').addEventListener("click",()=>{
        contenedorComoUsar.classList.remove('invisible');
        document.getElementById('cerrarComoUsar').addEventListener("click",()=>{
            contenedorComoUsar.classList.add('invisible');
        })
    })
})



