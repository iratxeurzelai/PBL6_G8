from random import randint
from Receta import *
import pandas as pd 

recetas = leerRecetas()

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