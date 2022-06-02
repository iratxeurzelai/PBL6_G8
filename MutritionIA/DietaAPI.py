from fastapi import FastAPI, Request
from AgenteDieta import *
import pandas as pd
from SimilarRecipeFinder import getAlternativas
from Usuario import *
from RecommenderSystem import *
import random

app=FastAPI()

recetas = leerRecetas() #para el csp
df = pd.read_csv('Recetas_procesadas.csv') #para recommender system y alternativa

usuariosDF = pd.read_csv('userPreferences.csv')
usuariosDF.drop('Unnamed: 0', axis=1, inplace=True)

dfMixta =crearDatasetMixta(df[['title', 'calories', 'protein', 'fat', 'sodium']], usuariosDF)


@app.post("/getDieta/")
def generarDieta(usuario: Usuario):
    solucion = calcularDietaSemana(recetas, usuario)
    solucion['usuario'] = usuario.id
    return solucion

@app.get("/shuffle/")
def generarDieta():
    random.shuffle(recetas)
    return "ok"

@app.post("/alternativa/{item_id}")
def generarDieta(item_id: int, usuario: Usuario):
    alternativas = getAlternativas(usuario, int(item_id), df, recetas)
    return alternativas

@app.post("/recommend/")
def generarDieta(usuario: Usuario):
    recomendacionNames = getRecomendacionReceta(usuario.peso, usuario.altura, dfMixta, usuariosDF)
    recomendadas = {} 
    recomendadas['Sabado'] = getRecetaIdByName(recomendacionNames[0], recetas)
    recomendadas['Domingo'] = getRecetaIdByName(recomendacionNames[1], recetas)
    recomendadas['usuario'] = usuario.id
    return recomendadas