import pandas as pd
from sklearn.metrics.pairwise import cosine_similarity
import numpy as np
from typing import Dict, List, Optional
import warnings
warnings.filterwarnings('ignore')
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import linear_kernel


def crearDatasetMixta(recetasDF: pd.DataFrame, usuariosDF: pd.DataFrame):
    #unir datos de usuarios con las recetas (on title de la receta)
    dfmixta = usuariosDF.merge(recetasDF, on='title')
    return dfmixta

def getRecomendacionReceta(peso: int, altura: int, dfmixta: pd.DataFrame, usuariosDF: pd.DataFrame):
    #get datos de usuario
    userinfo={"peso":[peso], "altura":  [altura]}

    #crear un dataset para guardar un unico usuario y calcular su cosine  similarity con el
    #dataframe de los usuarios para encontrar los que más se le parecen
    dt=pd.DataFrame(userinfo)
    user_sim_score=cosine_similarity(dt, usuariosDF[['peso', 'altura']])
    user_sim_score=user_sim_score[0]

    #crear una tuple para asociar una id al similarity score para tener un matriz así:
    #[0, 0.83]
    #[1, 0.77]
    #[2, 0.89] etc...
    users_sim_score_tuple: List[tuple] = []
    i = 0
    for x in user_sim_score:
        users_sim_score_tuple.append((i, x))
        i+=1

    #ordenar descendientemente por similarity y quedarnos solo con los 50 primeros
    sorted_by_score = sorted(users_sim_score_tuple, key=lambda tup: tup[1], reverse=True)
    top_50 = sorted_by_score[:50]

    #asociar la lista top_50 con las respectivas recetas
    recomendationDF = pd.DataFrame()
    for i in top_50:
        recomendationDF = recomendationDF.append(dfmixta.iloc[i[0]])

    #codificar texto
    #Defining a TF-IDF Vectorizer Object. Remove all english stop words such as 'the', 'a'
    tfidf = TfidfVectorizer(stop_words='english')
    #Constructing the required TF-IDF matrix by fitting and transforming the data
    tfidf_matrix = tfidf.fit_transform(recomendationDF['title'])

    #compute similarity matrix (similitud entre nombres de las top_50 recetas)
    cosine_sim = linear_kernel(tfidf_matrix, tfidf_matrix)

    #mirando la matriz de similitud entre las 50 recetas,
    #quedarse con la que más parecida sea comparandose con todas las demas
    indexLaMejor = 0
    mejorResultado = 0.0
    actual = 0.0

    for i in range (0, 50):
        for j in range(0, 50):
            actual+=cosine_sim[i,j]
        if actual > mejorResultado:
            mejorResultado=actual
            indexLaMejor=i
            actual=0
        else:
            actual=0.0

    receta_recomendar=recomendationDF[indexLaMejor:indexLaMejor+1] #un dataframe de 1 elemento
    recetaNP=receta_recomendar.to_numpy() #conertirlo a numpy
    recetaList = recetaNP.tolist() #pasarlo a lista compatible con json
    return recetaList