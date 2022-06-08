import sys,argparse,csv
from typing import Dict, List, Optional

class Receta:
    def __init__(self, name, calories, fat, protein, sodium, huevos, frutas, marisco, lacteos, frutos_secos, verduras, legumbres,
    vegetariano, primer_plato, carne):
        self.name = name
        self.calories = calories
        self.fat = fat
        self.protein = protein
        self.sodium = sodium
        self.huevos = huevos
        self.frutas = frutas
        self.marisco = marisco
        self.lacteos = lacteos
        self.frutos_secos = frutos_secos
        self.verduras = verduras
        self.legumbres = legumbres
        self.vegetariano =vegetariano
        self.primer_plato =primer_plato
        self.carne = carne

        #guardar las alergias para comprobarlas más facilmente
        self.alergias=[]
        if int(huevos)==1:self.alergias.append('Huevos')
        if int(marisco)==1:self.alergias.append('Marisco')
        if int(frutos_secos)==1:self.alergias.append('Frutos secos')
        if int(lacteos)==1:self.alergias.append('Lacteos')
        
        #logica inversa
        if int(vegetariano)==0:self.alergias.append('Vegetariano')

        #contiene
        self.contiene=[]
        if int(huevos)==1:self.contiene.append('Huevos')
        if int(marisco)==1:self.contiene.append('Marisco')
        if int(frutos_secos)==1:self.contiene.append('Frutos secos')
        if int(lacteos)==1:self.contiene.append('Lacteos')
        if int(carne)==1:self.contiene.append('Carne')
        if int(verduras)==1:self.contiene.append('Verduras')
        if int(legumbres)==1:self.contiene.append('Legumbres')
        if int(frutas)==1:self.contiene.append('Frutas')
        
    def __str__(self):
        return self.name + 'calories: ' + self.calories
    def __repr__(self):
        return self.name + 'calories: ' + self.calories

def leerRecetas():
    with open ("Recetas_procesadas.csv", encoding="utf8") as csv_file:
        csv_reader=csv.DictReader(csv_file,delimiter=',')
        recetas=[]
        for row in csv_reader:
                    receta = Receta(row['title'], row['calories'],row['fat'], row['protein'], row['sodium'], 
                    row['huevos'], row['frutas'], row['marisco'], row['lacteos'], row['frutos secos'], row['verduras'],
                    row['legumbres'], row['vegetariano'], row['primer plato'], row['carne'])
                    recetas.append(receta)
        csv_file.close()
        return recetas

def getRecetaIdByName(nombre: str, recetasLista: List[Receta]) -> int:
    id = 0
    i = 0
    for receta in recetasLista:
        if receta.name == nombre:
            id=i
        i+=1
    return id