from fastapi import FastAPI, File
from AgenteDieta import *

app=FastAPI()

@app.get("/getDieta/")
def generarDieta():
    solucion = calcularDietaSemana()
    return solucion
