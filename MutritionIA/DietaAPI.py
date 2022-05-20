from fastapi import FastAPI, Request
from AgenteDieta import *
from Usuario import *
import random

app=FastAPI()
recetas = leerRecetas()

@app.post("/getDieta/")
def generarDieta(usuario: Usuario):
    solucion = calcularDietaSemana(recetas, usuario)
    return solucion

@app.get("/shuffle/")
def generarDieta():
    random.shuffle(recetas)
    return "ok"