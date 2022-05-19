'use strict'
const urlServer = "http://localhost:8080";
//const urlServer = "https://taskcontrol.herokuapp.com";

document.addEventListener('DOMContentLoaded', function () { // codigo a ejecutar una vez que carga la pagina.
    document.getElementById('editarContenedor').classList.add('invisible'); // hace invisible el bloque 'editarContenedor'
    document.getElementById('eliminarContenedor').classList.add('invisible');// hace invisible el bloque 'eliminarContenedor'
    document.getElementById('editarEmpresaContenedor').classList.add('invisible');// hace invisible el bloque 'editarEmpresaContenedor'
    let btnEditarUsuario = document.getElementsByClassName('btnEditarUsuario');
    let btnEliminarUsuario = document.getElementsByClassName('btnEliminarUsuario');
    let idEmpleado = document.getElementsByClassName('idEmpleado');
    let nombreUsuarioCaja = document.getElementsByClassName('nombreUsuarioCaja');
    let idEmp = document.getElementById('idEmpr').value;// Toma el valor del id de la Empresa
    
    document.getElementById('editarEmpresa').addEventListener("click", ()=>{// Añade evento click a "editarEmpresa"
        editarEmpresa(idEmp);
    })
    
    for (let i = 0; i < btnEditarUsuario.length; i++) { // recorre el array de botones 'editar' de cada usuario
        btnEditarUsuario[i].addEventListener("click",()=>{//Asigna evento 'Click' a botones "editarUsuarios"
            editarUsuario(idEmpleado[i].value);
        })
        btnEliminarUsuario[i].addEventListener("click",()=>{ // Asigna evento 'Click' a botones 'eliminarUsuario'
            eliminarUsuario(idEmpleado[i+1].value, nombreUsuarioCaja[i+1].value);
        })
    }
})

//---------------------------------- EDITAR EMPRESA ----------------------------

async function editarEmpresa(idEmp){
    await fetch(urlServer+"/empresaRest/"+idEmp).// trae de la BD la empresa segun el ID correspondiente
        then(res => res.json()).
        then(datos => editarDatosEmpresa(datos));
    document.getElementById('editarEmpresaContenedor').classList.remove('invisible');// hace visible el menu 'editarEmpresa'
}

let idSesion = document.getElementById('idSesion').value;// ID del usuario en sesion

function editarDatosEmpresa(datos){ // inyecta los datos de la empresa en el Html para modificarlos.
    document.getElementById('formEditarEmpresa').innerHTML = `
    <h2>Editar datos de empresa.</h2><br>
    <label for="nombre">Nombre</label>
    <input value="${datos.nombre}" type="text" name="nombreEmpr" required><br><br>
    
    <label for="rubroEmpr">Rubro</label>
    <input value="${datos.rubro}" type="text" name="rubroEmpr" required><br><br>
    
    <label for="actividadEmpr">Actividad</label>
    <input value="${datos.actividad}" type="text" name="actividadEmpr" required><br><br>
    
    <label for="archivoUsuario">Seleccione una imagen de usuario</label>
    <input value="${datos.foto}" type="file" name="archivoUsuario" ><br><br>
    
    <input type="number" name="id" value="${datos.id}" hidden>
    <input type="number" name="idSesion" value="${idSesion}" hidden>

    <button type="submit">Guardar</button>`;
}

document.getElementById('cerrarEditarEmpresa').addEventListener("click", ()=>{// accion para la 'X' que cierra la edicion
    document.getElementById('editarEmpresaContenedor').classList.add('invisible');
})
//----------------------------------------------------------------------------

//---------------------------------- EDITAR USUARIO ----------------------------

async function editarUsuario(idEmp){
    await fetch(urlServer+"/usuario/"+idEmp).// trae de la BD el usuario segun el ID correspondiente
        then(res=> res.json()).
        then(datos => editarDatos(datos));

    function editarDatos(datos){ // inyecta los datos del usuario en el Html para modificarlos.
        document.getElementById('formEditarUsuario').innerHTML = `
        <h2>Editar datos de usuario.</h2><br>
        <label for="nombre">Nombre</label>
        <input value="${datos.nombre}" type="text" name="nombre" required><br><br>
        <label for="apellido">Apellido</label>
        <input value="${datos.apellido}" type="text" name="apellido" required><br><br>
        <label for="dni">dni</label>
        <input value="${datos.dni}" type="text" name="dni" maxlength="9" onKeypress="if (event.keyCode < 45 || event.keyCode > 57) event.returnValue = false;" required ><br><br>
        <label for="archivoUsuario">Seleccione una imagen de usuario</label>
        <input value="${datos.foto}" type="file" name="archivoUsuario" ><br><br>
        <label for="email">Email</label>
        <input value="${datos.email}" type="text" name="email" required><br><br>
        <label for="puesto">Puesto</label>
        <input value="${datos.puesto}" type="text" name="puesto" required><br><br>
        <input type="number" name="id" value="${datos.id}" hidden>

        <button type="submit">Guardar</button>`
    }
    document.getElementById('editarContenedor').classList.remove('invisible');// Hace visible el menu 'editar Usuario'
    document.getElementById('cerrarEditarUsuario').addEventListener("click", ()=>{
        document.getElementById('editarContenedor').classList.add('invisible'); // accion para la 'X' que cierra la edicion
    })
}
//----------------------------------------------------------------------------

//---------------------------------- ELIMINAR USUARIO ----------------------------

function eliminarUsuario(idEmp, nombreUsuarioCaja){ // inyecta en el Html la pregunta de confirmacion de eliminacion
    document.getElementById('subContEliminar').innerHTML = `
    <h3 style="text-align: center;">Desea eliminar a ${nombreUsuarioCaja}?</h3>
    <form action="/eliminarUsuario" method="post" id="formEliminar">
        <input type="number" name="idEmp" value="${idEmp}" hidden>
        <button type="submit" id="btnSi">SI</button>
        <button id="btnNo" type="button">NO</button>
    </form>`; // El boton 'SI' elimina al usuario, y el boton 'NO' cierra el cartel de confirmacion
    
    document.getElementById('eliminarContenedor').classList.remove('invisible'); // Hace visible el bloque 'eliminar usuario'
    document.getElementById('eliminarContenedor').classList.add('displayFlexCentrar');// agrega una clase para centrar elementos
    document.getElementById('btnNo').addEventListener("click", ()=>{ // asigna funciones al boton 'NO' de eliminar usuario.
        document.getElementById('eliminarContenedor').classList.remove('displayFlexCentrar');// Quita la clase recien agregada
        document.getElementById('eliminarContenedor').classList.add('invisible');// Hace visible el bloque 'eliminar usuario'
    })
}
//----------------------------------------------------------------------------

let idEmp = document.getElementById('idEmpr').value; // obtiene el ID de la empresa en curso
llenarCantUsuarios(idEmp);

//----------- Comprueba cantidad de usuarios que tiene la empresa, si el numero llega a 6 (incluido el Lider),
//----------- la app no permite seguir agregando usuarios, y ofrece la opcion premium (en desarrollo).

async function llenarCantUsuarios(idEmp){
    await fetch(urlServer+"/empresaRest/"+idEmp). // busca en la BD la empresa segun ID
    then(res=> res.json()).
        then(datos => {
            if(datos.cantUsuarios<5){
                document.getElementById('cantidadUsuarios').innerHTML = `<a href="/crearUsuario" > Agregar usuario a ${datos.nombre}</a>`;
            } else {
                document.getElementById('cantidadUsuarios').innerHTML = `<h3 >La empresa ya tiene el maximo de 5 usuarios, elija la version Premium</h3>`;
            }
        });
}
//----------------------------------------------------------------------------
