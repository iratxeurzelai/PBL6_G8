from csp import Constraint, CSP
from typing import Dict, List, Optional
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
        return self.name 

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
        
#Implementaciones de la clase abstracta Constraint definida en csp.py
#Una implementación para cada tipo de constraint que vamos a incluir
#La funcion satisfied devuelve True si el asgnamiento es valido, y False si no.
#Cada asignaiento el csp comprueba las constraints, si devuelve False, replanifica.

class CaloriesConstraint(Constraint[str, Receta]):
    def __init__(self, diaP: str, diaS) -> None:
        super().__init__([diaP, diaS])
        self.diaP: str = diaP
        self.diaS: str = diaS

    def satisfied(self, assignment: Dict[str, Receta]) -> bool:
        if self.diaP not in assignment or self.diaS not in assignment:
            return True
        if float(assignment[self.diaP].calories)+float(assignment[self.diaS].calories) in range (1000, 1500):
            return True
        else:
            return False

class MacroConstraint(Constraint[str, Receta]):
    def __init__(self, diaAnteriorP: str, dia1S, diaP, diaS) -> None:
        super().__init__([diaAnteriorP, dia1S, diaP, diaS])
        self.diaAnteriorP: str = diaAnteriorP
        self.diaAnteriorS: str = dia1S
        self.diaP: str = diaP
        self.diaS: str = diaS

    def satisfied(self, assignment: Dict[str, Receta]) -> bool:
        if self.diaAnteriorP not in assignment or self.diaAnteriorS not in assignment or self.diaP not in assignment or self.diaS not in assignment:
            return True
        
        #Calcular los valores maximos y minimos de los macros, haciendo uso del dia anterior
        #Para luego decidir si devolver True o False.

        #proteinas
        maxProteinas=130
        if (float(assignment[self.diaAnteriorP].protein)+float(assignment[self.diaAnteriorS].protein)) < 50: #20 40
            minProteinas = 60 #40
        else:
            minProteinas=20 #20
            if float(assignment[self.diaAnteriorP].protein) >100: #100 130
                maxProteinas = 65 #90
            else:
                maxProteinas=130 #130
        proteinasHoy=float(assignment[self.diaP].protein)+float(assignment[self.diaAnteriorS].protein)

        #Fat
        maxFat=180
        if (float(assignment[self.diaAnteriorP].fat)+float(assignment[self.diaAnteriorS].fat)) < 300: #100 120
            minFat = 20 #140
        else:
            minFat=10 #100
            if float(assignment[self.diaAnteriorP].fat) >130: #160 180
                maxFat = 180 #150
            else:
                maxFat=180 #180
        fatHoy=float(assignment[self.diaP].fat)+float(assignment[self.diaAnteriorS].fat)
    
        #Sodium
        maxSodio=3000
        if (float(assignment[self.diaAnteriorP].protein)+float(assignment[self.diaAnteriorS].protein)) < 50: #20 40
            minSodio = 2100 #40
        else:
            minSodio=1700 #20
            if float(assignment[self.diaAnteriorP].protein) > 130: #100 130
                maxSodio = 2500 #90
            else:
                maxSodio=3100 #130
        sodiumHoy=float(assignment[self.diaP].sodium)+float(assignment[self.diaAnteriorS].sodium)

        #constraint
        if proteinasHoy in range (minProteinas, maxProteinas) and fatHoy in range (minFat, maxFat) and sodiumHoy in range (minSodio, maxSodio):
            return True
        else:
            return False

class NoRepetirComidaConstraint(Constraint[str, Receta]):
    def __init__(self, dia1, dia2) -> None:
        super().__init__([dia1, dia2])
        self.dia1: str = dia1
        self.dia2: str = dia2

    def satisfied(self, assignment: Dict[str, Receta]) -> bool:
        if self.dia1 not in assignment or self.dia2 not in assignment:
            return True
        return assignment[self.dia1]!=assignment[self.dia2]

class PrimerPlatoConstraint(Constraint[str, Receta]):
    def __init__(self, dia: str) -> None:
        super().__init__([dia])
        self.dia: str = dia

    def satisfied(self, assignment: Dict[str, Receta]) -> bool:
        if int(assignment[self.dia].primer_plato)==1:
            return True
        else:
            return False

class SegundoPlatoConstraint(Constraint[str, Receta]):
    def __init__(self, dia: str) -> None:
        super().__init__([dia])
        self.dia: str = dia

    def satisfied(self, assignment: Dict[str, Receta]) -> bool:
        if int(assignment[self.dia].primer_plato)==0:
            return True
        else:
            return False


#Funcion main
if __name__ == "__main__":
    recetas = leerRecetas()

    #declarar variables y dominios (Dias y Recetas)
    variables: List[str] = ["LunesP", "LunesS", "MartesP", "MartesS", "MiercolesP", "MiercolesS", "JuevesP", "JuevesS",
     "ViernesP", "ViernesS"]
    domains: Dict[str, List[Receta]] = {}
    for variable in variables:
        domains[variable] = recetas
    csp: CSP[str, List[Receta]] = CSP(variables, domains)
   
   #constraints primer plato
    i=0
    while i <= len(variables)-2:
        csp.add_constraint(PrimerPlatoConstraint(variables[i]))
        i+=2 
        
    i=0
    while i <= len(variables)-2:
        csp.add_constraint(SegundoPlatoConstraint(variables[i+1]))
        i+=2 

   #contraints calorias
    i=0
    while i <= len(variables)-2:
        csp.add_constraint(CaloriesConstraint(variables[i], variables[i+1]))
        i+=2

    #contraints macros
    i=2
    while i <= len(variables)-2:
        csp.add_constraint(MacroConstraint(variables[i-2], variables[i-1], variables[i], variables[i+1]))
        i+=2
    
    #constraint no repetir
    for i in range(0, len(variables)-1):
        for j in range (i+1, len(variables)):
            csp.add_constraint(NoRepetirComidaConstraint(variables[i], variables[j]))


    #calcular una solucion
    solution: Optional[Dict[str, Receta]] = csp.backtracking_search()
    if solution is None:
        print("No solution found!")
    else:
        #print(solution)
        print("{" + "\n".join("{!r}: {!r},".format(k, v) for k, v in solution.items()) + "}")