from random import randint
from Receta import *
import pandas as pd 
import random

def decision(probability):
    return random.random() < probability

#Para el recomender system inicial
def func1(recetas):
    pesos = []
    alturas = []
    recetasFav = []
    for i in range (0, 2000):
        pesos.append(randint(45, 100))
        alturas.append(randint(150, 200))
        recetasFav.append(recetas[randint(0, len(recetas)-1)].name)


    # dictionary of lists  
    dict = {'peso': pesos, 'altura': alturas, 'title': recetasFav}  
        
    df = pd.DataFrame(dict) 
        
    # saving the dataframe 
    df.to_csv('userPreferences.csv') 

    print('fin')

#Para el colaborative filtering (el de Likes)
def func2(recetas):
    dict = {}
    dict['userid'] = []
    dict['recetaid'] = []
    dict['like'] = []
    for userid in range (0, 1000):
        for recetaid in range (0, len(recetas)):
            if (decision(0.07)):
                dict['userid'].append(userid)
                dict['recetaid'].append(recetaid)
                if (decision(0.55)):
                    dict['like'].append(1)
                else:
                    dict['like'].append(1)
        
    df = pd.DataFrame(dict) 
        
    # saving the dataframe 
    df.to_csv('userLikes.csv') 
    print('fin')

#Para el colaborative filtering (el de Likes) \ No aleatorio
def funcNoAleatoria(recetas):
    dict = {}
    dict['userid'] = []
    dict['recetaid'] = []
    dict['like'] = []
    #primera mitad de usuarios
    for userid in range (0, 500):
        #primera mitad de recetas alta probabilidad de like
        for recetaid in range (0, int(len(recetas)/2)):
            if (decision(0.2)):
                dict['userid'].append(userid)
                dict['recetaid'].append(recetaid)
                if (decision(0.85)):
                    dict['like'].append(1)
                else:
                    dict['like'].append(0)
        #segunda mitad de recetas baja probabilidad de like
        for recetaid in range (int((len(recetas)/2)+1), len(recetas)):
            if (decision(0.2)):
                dict['userid'].append(userid)
                dict['recetaid'].append(recetaid)
                if (decision(0.15)):
                    dict['like'].append(1)
                else:
                    dict['like'].append(0)
    #Segunda mitad de usuarios
    for userid in range (500, 1000):
        #primera mitad de recetas baja probabilidad de like
        for recetaid in range (0, int(len(recetas)/2)):
            if (decision(0.2)):
                dict['userid'].append(userid)
                dict['recetaid'].append(recetaid)
                if (decision(0.15)):
                    dict['like'].append(1)
                else:
                    dict['like'].append(0)
        #segunda mitad de recetas alta probabilidad de like
        for recetaid in range (int(len(recetas)/2+1), len(recetas)):
            if (decision(0.2)):
                dict['userid'].append(userid)
                dict['recetaid'].append(recetaid)
                if (decision(0.85)):
                    dict['like'].append(1)
                else:
                    dict['like'].append(0)

    df = pd.DataFrame(dict)  
    # saving the dataframe 
    df.to_csv('userLikes.csv') 
    print('fin')

recetas = leerRecetas()
funcNoAleatoria(recetas)