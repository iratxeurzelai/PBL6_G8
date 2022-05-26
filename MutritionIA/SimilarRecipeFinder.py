from Receta import *
from typing import Dict, List, Optional
import pandas as pd
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity
from Usuario import Usuario

def revisarAlergias(alergiasUsuario: List[str], alergiasRecetas: List[str]) -> bool:
    for alergia in alergiasUsuario:
            if alergiasRecetas.__contains__(alergia): return True
    return False

def getAlternativas(usuario: Usuario, indexReceta: int, df: pd.DataFrame, recetas: List[Receta]):
    similarityMatrix=cosine_similarity(df[['calories', 'protein', 'fat', 'sodium']]) #similitud en los macros
    similar_food = list(enumerate(similarityMatrix[indexReceta])) #crear una tuple (index, similitud)
    sorted_similar_food = sorted(similar_food, key=lambda x:x[1], reverse=True) #ordenar por similitud

    listaAlternativas = []
    asignadas=0
    i=0

    while asignadas < 5:
        indexAlternativa=sorted_similar_food[i][0]
        i+=1 
        
        if recetas[indexAlternativa]==recetas[indexReceta]: #no devlover la misma receta
            pass
        elif recetas[indexAlternativa].primer_plato!=recetas[indexReceta].primer_plato: #que coincida el tipo de plato
            pass
        elif revisarAlergias(recetas[indexAlternativa].alergias, usuario.alergias): #que no tenga alergias a esa receta
            pass
        else:
            listaAlternativas.append(recetas[indexAlternativa])
            asignadas+=1

    return listaAlternativas