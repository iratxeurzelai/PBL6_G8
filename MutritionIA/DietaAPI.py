from fastapi import FastAPI, Request
from AgenteDieta import *
from Usuario import *

app=FastAPI()
recetas = leerRecetas()

@app.post("/getDieta/")
def generarDieta(usuario: Usuario):
    #usuario = json.loads(open('usuarioPrueba.json').read())
    #usuario = json.loads(usuarioJson)
    solucion = calcularDietaSemana(recetas, usuario)
    return solucion


# @app.post("/getDieta/")
# def generarDieta(usuarioJson: Request):
#     usuario = usuarioJson.json()
#     alergias=usuario['alergias']
#     print(alergias)
#     solucion = calcularDietaSemana(recetas, usuario)
#     return solucion
