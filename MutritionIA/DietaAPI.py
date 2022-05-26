from fastapi import FastAPI, Request
from AgenteDieta import *
import pandas as pd
from SimilarRecipeFinder import getAlternativas
from Usuario import *
import random

app=FastAPI()
recetas = leerRecetas()
df = pd.read_csv('Recetas_procesadas.csv')

@app.post("/getDieta/")
def generarDieta(usuario: Usuario):
    solucion = calcularDietaSemana(recetas, usuario)
    return solucion

@app.get("/shuffle/")
def generarDieta():
    random.shuffle(recetas)
    return "ok"

@app.post("/alternativa/{item_id}")
def generarDieta(item_id: int, usuario: Usuario):
    alternativas = getAlternativas(usuario, int(item_id), df, recetas)
    return alternativas