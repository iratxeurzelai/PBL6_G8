import sys,argparse,csv
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
  