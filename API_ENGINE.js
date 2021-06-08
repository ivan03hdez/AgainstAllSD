const express = require("express");
const fs=require("fs");
const app = express(); 
const path = require("path");
const DIRECTORY = '';
const JSONFILENAME = 'usuario.json';
const FILENAME = 'estadoUsuarios.txt';
const FILEMAPA='mapa.txt';

// Se define el puerto 
const port=3000; 
app.get("/",(req, res) => {
	res.json({message:'Página de inicio de aplicación de ejemplo de SD'}) }); 
	// Ejecutar la aplicacion
app.listen(port, () => {    
	console.log(`Ejecutando  la  aplicación  API  REST  de  SD  en  el  puerto ${port}`); 
});


app.get("/usuarios",(request,response)=>{
	try { 
		datos = createJsonFile();
		//const datos = fs.readFileSync(DIRECTORY + JSONFILENAME,'utf8'); 
		response.send(JSON.parse(datos)); 
	 } catch (error) { 
		console.log (error); 
	 } 
});

app.get("/mapa",(request,response)=>{
	var options={root:path.join(__dirname)};
	
	try{ 
		response.sendFile(FILEMAPA,options,function(err){
			if(err){
				response.json({message:'No se ha podido abrir la base de datos y no se ha podido mostrar mapa'});
			}
			else{
				console.log("Fichero enviado");
			}
			}); 
	 } catch (error) { 
		console.log (error); 
	 }
 
});
////////////////////////////////////////////////////////////
///////DEBERIAMOS CREAR UN FICHERO DE USUARIOS NUEVO CON SU ESTADO ACTUAL DE LA PARTIDA (NIVEL ACTUAL/VIVO O MUERTO...)
function isEmpty(str) {
    return (!str || str.length === 0 );
}

function createJsonFile() {
	var jsonData = "{";
	var aux;
	const keys = ["alias", "pasword", "nivelInicio","nivelActual", "EF", "EC","estado"];
	var lines = fs.readFileSync(DIRECTORY + FILENAME).toString();
	if(isEmpty(lines)){
		return "{\"mensaje\": \"No se ha unido ningun usuario a la partida\"}";
	}
	lines=lines.split(/[\n\r]/);
	lines.forEach(function (valor, indice, array) {
		if(valor !== ""){
			var i = 0;
			var arrayDatos = valor.split(":");
			arrayDatos = arrayDatos.reduce(function (resultado, valor) {
				resultado[keys[i]] = valor;
				i++;
				return resultado;
			}, {});
			jsonData += "\"usuario" + (indice + 1) + "\":";
			var json = JSON.stringify(arrayDatos, null, '\t');
			jsonData += json + ",";
		}	
	});
	jsonData = jsonData.substring(0, jsonData.length-1);
	jsonData += "}";
	return jsonData;
}




