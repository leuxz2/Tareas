'use strict'
const urlServer = "http://localhost:8080";
//const urlServer = "https://taskcontrol.herokuapp.com";

// -------------------------- Acciones a ejecutar cuando se carga el documento HTML---------------------------

document.addEventListener('DOMContentLoaded', function () {
    mostrarTareas(0);
    let empleados = document.getElementById('empleados'); // desplegable Select de empleados para ver tareas 
    document.getElementById('tablaAmpliada').classList.add('invisible'); // hace invisible a "ampliar tabla"
    document.getElementById('contenedorNota').classList.add('invisible'); // hace invisible a 'ver Nota' 
    document.getElementById('contenedorCrearNota').classList.add('invisible'); // hace invisible a 'crear Nota'
    document.getElementById('contenedorCrearTarea').classList.add('invisible'); // hace invisible a 'crear Tarea'
    document.getElementById('btnCrearTarea').addEventListener("click", ()=>{ // asignamos accion al boton 'Crear Tarea'
        crearTarea();
    })
    let id = document.getElementById('idSesion').value; // valor del id del usuario en Sesion
    let urlEmp = urlServer+"/usuario/empleados/"; // Api devuelve lista de empleados de empresa por id de Lider
    traerDatos(urlEmp,id);
    
    async function traerDatos(urlEmp,id){ // Trae lista de empleados y los inyecta en la Funcion 'usuarioOption'
        await fetch (urlEmp+id)
                    .then(res => res.json())
                    .then(datos => {
                        if (datos!=null) {
                            usuarioOption(datos);
                        }             
                    })
    }
    
    function usuarioOption(datos){ // itera entre los empleados y los inyecta como 'option' en el Select 'empleados'
        let info = "";
        datos.forEach(usuario =>{
           let usuarios = `<option value="${usuario.id}">${usuario.nombre}</option>`
           info += usuarios;
        })
        empleados.innerHTML = info; // inyecta los empleados en el Select 'empleados'
        document.getElementById('empleadosDuplic').innerHTML = info; // repite en el Select 'empleadosDuplic' para crear Tarea
        }

})

//---------------------------- para llenar la tabla ------------------------------------

const li = document.querySelectorAll('.li'); // array de 'li' para 'Todas tareas', 'Tareas cumplidas', 'Tareas incumplidas'
const bloque = document.getElementsByTagName('bloque'); // bloque de tareas
let tablaDatos = document.getElementById('tablaDatos'); // tbody para inyectar las tareas.
li[0].classList.add('activo'); // activa en principio a la primer pestaña "Todas las tareas" 

// Recorremos todos los li
li.forEach( (cadaLi , i)=> {
    li[i].addEventListener('click', ()=>{ // Asignamos a cada li un evento de click

        li.forEach( (cadaLi, i)=>{ // recorremos nuevamente los 'li' para quitarles la clase 'activo'
            li[i].classList.remove('activo'); // A todos los li se les quita la clase 'activo'
        })

        li[i].classList.add('activo') // Al li que se hizo click le añadimos la clase 'activo'
        tablaDatos.innerHTML = `<h3>No se encontraron tareas.</h3>`; // por defecto escribimos esto en el tBody
        mostrarTareas(i); 
    })

})
let idEmpleado = document.getElementById('idSesion').value; // id del usuario en sesion

document.getElementById('verTareas').addEventListener("click",()=>{ // click en 'ver tareas' sucedera lo siguiente:
    idEmpleado = document.getElementById('empleados').value; // id del empleado seleccionado en el Select 'empleados'
    document.getElementById('tablaAmpliada').classList.add('invisible'); // ocultamos 'tabla ampliada'
    document.getElementById('verTareas').classList.remove('resaltado'); // removemos el resaltado en 'ver tareas'
    document.getElementById('empleadoActual').classList.remove('resaltado'); // removemos el resaltado en 'empleadoActual'

    li[1].classList.remove('activo'); // desactivamos 'tareas cumplidas' por si estaba activo
    li[2].classList.remove('activo'); // desactivamos 'tareas incumplidas' por si estaba activo
    li[0].classList.add('activo'); // se mostrara siempre activo el 'todas las tareas' al hacer click en 'ver tareas'
    tablaDatos.innerHTML = `<h3>No se encontraron tareas.</h3>` // inyectamos esto por defecto, si hay tareas se sobreescribira
    mostrarTareas(0); // queremos mostrar todas las tareas, por eso el valor 0
})



function mostrarTareas(i){ // segun si queremos todas las tareas, cumplidas o incumplidas es la Api que consultara
    let url = "";

    switch (i) {
        case 0: // Todas las tareas (0)
            url = urlServer+"/tareaRest/";   
            break;

        case 1: // Solo tareas cumplidas (1)
            url = urlServer+"/tareaRest/cumplidas/";     
            break;

        case 2: // Solo tareas INcumplidas (2)
            url = urlServer+"/tareaRest/incumplidas/"; 
            break;
    
        default:
            break;
    }

    traerDatosTareas(url) // enviamos la Url que corresponda segun si necesitamos todas las tareas, cumplidas o incumplidas
    
}



function traerDatosTareas(url){ // traemos las tareas correspondientes del empleado elegido en el Select 'empleado'
    fetch (url+idEmpleado)
                .then(res => res.json())
                .then(datos => {
                    if (datos!=null) {
                        tabla(datos); // enviamos la lista de tareas obtenida para ser inyectada al Html
                    } else {
                        tablaDatos.innerHTML = `<h3>No se encontraron tareas.</h3>`;
                    }               
                })
            
    fetch(urlServer+`/usuario/${idEmpleado}`) // trae al empleado actual y lo inyecta al Html
                .then(res => res.json())
                .then(datos => {
                    document.getElementById('empleadoActual').innerHTML= datos.nombre;
                })
}
//--------------------------

function tabla(datos){ // itera todas las tareas y las inyecta en 'tabla datos', que es el Tbody de la tabla de tareas.
    let info = "";
    
    datos.forEach(tarea=>{
       let cumplir = tarea.cumplida == true ? '<img class="cumplida" src="/img/tildeVerde.png" alt="Cumplida">' : '<img class="cumplida" src="/img/equisRoja.png" alt="NoCumplida">';
       let nota = tarea.nota == null ? "Agregar nota" : "Ver nota";
       let tareas = `<tr>`+
                        `<input type="number" class="idTarea" value='${tarea.id}' hidden>`+
                        `<th scope="row" class="campoId" >${tarea.id}</th>`+
                        `<td class="campoNombre">${tarea.nombre} </td>`+
                        `<td class="campoDescripcion">${tarea.descripcion} </td>`+
                        `<td class="campoCreacion">${tarea.fechaCreacion} </td>`+
                        `<td class="campoLimite">${tarea.fechaFinal} </td>`+
                        `<td class="campoCumplida">${cumplir} </td>`+
                        `<td ><div class="acciones">`+
                        `<a class="ampliar" > <img src="/img/lupa blanca.png"  title="Ampliar" class="accion"></a>`+
                        `<a class="cumplirTarea" href="/tarea/cumplir/${tarea.id}" ><img src="/img/tildeBlanco.png"  id="cumplir" title="Dar por Resuelta" class="accion"></a>`+
                        `<a class="eliminarTarea"><img src="/img/eliminar.png"  id="eliminar" title="Eliminar tarea" class="accion"></a>`+    
                        `</div ></td>`+
                        `<td class="nota campoNota" >${nota}</td>`+     
                             `</tr>`;

       info += tareas;
    })
    document.getElementById('selecEmpleado').classList.remove('invisible'); // Solo util para usuario NO lider
    let eliminarTarea = document.getElementsByClassName('eliminarTarea'); // Solo util para usuario NO lider
        for (let i = 0; i < eliminarTarea.length; i++) {
            eliminarTarea[i].classList.add('invisible'); // Solo util para usuario NO lider
                                
        }
    tablaDatos.innerHTML = info;
    asignarClickAcciones(); //una vez llena las tareas, se asignan las acciones a los botones de 'ampliar','eliminar',etc
    
    let idSesion = document.getElementById('idSesion').value; // Toma el Id Sesion para la siguiente funcion
    restringirUsuario(idSesion); // Restringe 'Eliminar Tarea' para usuario comun (No lider) 
    }
    

    //-------------------------
empleados.addEventListener("change", opcionCambiada); // cuando el Select 'empleado' cambia de estado ...
function opcionCambiada (){ // funcion para resaltar lo que se debe observar al cambiar de empleado en el Select
    document.getElementById('verTareas').classList.add('resaltado');
    document.getElementById('empleadoActual').classList.add('resaltado'); 
}
//--------------------------

function asignarClickAcciones(){ // funcion para reasignar las acciones a cada boton, una vez llenada la tabla de tareas
    
    let ampliar = document.getElementsByClassName("ampliar"); // todas las lupas para ampliar tarea
    let idTarea = document.getElementsByClassName("idTarea"); // todos los id de cada tarea
    let nota = document.getElementsByClassName("nota"); // todas las notas (ver o crear) de cada tarea
    let eliminarTarea = document.getElementsByClassName('eliminarTarea'); // todas las cruces para eliminar cada tarea
    
    for (let i = 0; i < ampliar.length; i++) {
        ampliar[i].addEventListener("click", ()=>{ // asignamos accion a cada boton 'ampliar'
        document.getElementById('tablaAmpliada').classList.add('invisible') 
        ampliarTarea(idTarea[i].value);
        });
        
        nota[i].addEventListener("click", ()=>{ // asignamos accion a cada boton 'notas'
            notaVerCrear(idTarea[i].value);
        })

        eliminarTarea[i].addEventListener("click", ()=>{ // asignamos accion a cada boton 'eliminar'
            if (confirm(`Desea eliminar la tarea Código: ${idTarea[i].value}`)) {
                window.location.href = `/tarea/eliminar/${idTarea[i].value}`;
            } else {
                window.location.href = `/inicio`;
                
            }
        })
    }
    
}
//-----------------------------------AMPLIAR TAREA -----------------------

async function ampliarTarea(idTareaSeleccionada){
    
    await fetch (urlServer+"/tareaRest/tarea/"+idTareaSeleccionada)
                .then(res => res.json())
                .then(tarea => {
                    if (tarea!=null) { 
                        let tareaAmpliada = `<h3>Tarea código: </h3><b>${tarea.id}</b><br>
                                            <h3>Nombre: </h3><b>${tarea.nombre}</b><br>
                                            <h3>Descripción: </h3><b>${tarea.descripcion}</b>`;
                        document.getElementById('tareaAmpliada').innerHTML = tareaAmpliada;
                        document.getElementById('tablaAmpliada').classList.remove('invisible');
                    }             
                })
}

document.getElementById('cerrarAmpliada').addEventListener("click",()=>{
    document.getElementById('tablaAmpliada').classList.add('invisible')
})
//---------------------- NOTA DE TAREA -------------

async function notaVerCrear(idTarea){
    
    fetch(urlServer+"/tareaRest/tarea/"+idTarea)
    .then(res => res.json())
    .then(tarea => {
        if (tarea.nota!=null) {
            mostrarNota(tarea.nota)
        } else {
            crearNota(tarea.id);
        }
    }) 
}

function mostrarNota(nota){
    document.getElementById('notaMostrada').innerHTML = nota; // inyecta la nota obtenida
    document.getElementById('contenedorNota').classList.remove('invisible'); // hace visible el contenedor de la nota
}

document.getElementById('cerrarNota').addEventListener("click",()=>{ // accion para la cruz (cerrar) de mostrar nota
    document.getElementById('contenedorNota').classList.add('invisible');
})

function crearNota(idTarea){ // inyecta el formulario para crear nota 
    document.getElementById('formNota').innerHTML= `<h2>Ingrese la nota para la Tarea código: ${idTarea}</h2>
                                                    <form action="/tarea/crearNota" method="post">
                                                        <input type="number" name="id" value="${idTarea}" hidden><br>
                                                        <textarea name="nota"  cols="50" rows="4" maxlength="200" placeholder="Ingrese el texto" required></textarea><br><br>
                                                        <button type="submit" class="btnGuardar">Guardar</button>
                                                    </form>`;
    document.getElementById('contenedorCrearNota').classList.remove('invisible');
}

document.getElementById('cerrarCrearNota').addEventListener("click",()=>{ // accion para la cruz (cerrar) de crear nota
    document.getElementById('contenedorCrearNota').classList.add('invisible');
})
//------------------------ CREAR TAREA -----------------

function crearTarea(){
    document.getElementById('contenedorCrearTarea').classList.remove('invisible'); // hace visible el form 'crear tarea'
}

document.getElementById('cerrarCrearTarea').addEventListener("click",()=>{ // accion para la cruz (cerrar) de 'crear tarea'
    document.getElementById('contenedorCrearTarea').classList.add('invisible');
})

//-------Comprueba si el usuario en sesion es el lider de la empresa, si no lo es, se bloquean funciones ---------------------------------------

async function restringirUsuario(idSesion){ 
    
    await fetch (urlServer+"/usuario/empleados/"+idSesion)
                    .then(res => res.json())
                    .then(datos => {
                        if (datos[0].id != idSesion) {
                            document.getElementById('selecEmpleado').classList.add('invisible');
                            let eliminarTarea = document.getElementsByClassName('eliminarTarea');
                            for (let i = 0; i < eliminarTarea.length; i++) {
                                eliminarTarea[i].classList.add('invisible');
                                
                            }
                        }             
                    })
}

//--------------------------------

