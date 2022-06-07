from turtle import pos
from csp import Constraint, CSP, Preference
from Receta import *
from typing import Dict, List, Optional
from Usuario import *
import sys,argparse
    
#Implementaciones de la clase abstracta Constraint definida en csp.py
#Una implementación para cada tipo de constraint que vamos a incluir
#La funcion satisfied devuelve True si el asgnamiento es valido, y False si no.
#Cada asignaiento el csp comprueba las constraints, si devuelve False, replanifica.

class CaloriesConstraint(Constraint[str, Receta]):
    def __init__(self, diaP: str, diaS, usuario: Usuario) -> None:
        super().__init__([diaP, diaS])
        self.diaP: str = diaP
        self.diaS: str = diaS
        self.usuario=usuario
        
        self.maxCalorias= int(float(usuario.peso)*17.14+float(usuario.altura)*1.71);

    def satisfied(self, assignment: Dict[str, Receta]) -> bool:
        if self.diaP not in assignment or self.diaS not in assignment:
            return True
        if float(assignment[self.diaP].calories)+float(assignment[self.diaS].calories) in range (self.maxCalorias-500, self.maxCalorias):
            return True
        else:
            return False

class MacroConstraint(Constraint[str, Receta]):
    def __init__(self, diaAnteriorP: str, dia1S, diaP, diaS, usuario: Usuario) -> None:
        super().__init__([diaAnteriorP, dia1S, diaP, diaS])
        self.diaAnteriorP: str = diaAnteriorP
        self.diaAnteriorS: str = dia1S
        self.diaP: str = diaP
        self.diaS: str = diaS
        self.usuario=usuario

    def satisfied(self, assignment: Dict[str, Receta]) -> bool:
        if self.diaAnteriorP not in assignment or self.diaAnteriorS not in assignment or self.diaP not in assignment or self.diaS not in assignment:
            return True
        
        #Calcular los valores maximos y minimos de los macros, haciendo uso del dia anterior
        #Para luego decidir si devolver True o False.

        #proteinas
        maxProteinas=int(self.usuario.peso*1.85)#130
        if (float(assignment[self.diaAnteriorP].protein)+float(assignment[self.diaAnteriorS].protein)) < self.usuario.peso*0.71:#50 
            minProteinas = int(self.usuario.peso*0.85) #60
        else:
            minProteinas=int(self.usuario.peso * 0.28) #20
            if float(assignment[self.diaAnteriorP].protein) >self.usuario.peso * 1.4: #100
                maxProteinas = int(self.usuario.peso*0.93) #65
            else:
                maxProteinas=int(self.usuario.peso*1.85) #130
        proteinasHoy=float(assignment[self.diaP].protein)+float(assignment[self.diaAnteriorS].protein)

        #Fat
        maxFat=int(self.usuario.peso*2.57)#180
        if (float(assignment[self.diaAnteriorP].fat)+float(assignment[self.diaAnteriorS].fat)) < self.usuario.peso*4.28: #300
            minFat = int(self.usuario.peso*0.28) #20
        else:
            minFat=int(self.usuario.peso*0.14) #10
            if float(assignment[self.diaAnteriorP].fat) >self.usuario.peso*1.85: #130
                maxFat = int(self.usuario.peso*2.57)#180
            else:
                maxFat=int(self.usuario.peso*2.57) #180
        fatHoy=float(assignment[self.diaP].fat)+float(assignment[self.diaAnteriorS].fat)
    
        #Sodium
        maxSodio=int(self.usuario.peso*42.85)#3000
        if (float(assignment[self.diaAnteriorP].protein)+float(assignment[self.diaAnteriorS].protein)) < self.usuario.peso*14.28: #1000
            minSodio = int(self.usuario.peso*30.0) #2100
        else:
            minSodio=int(self.usuario.peso*24.28) #1700
            if float(assignment[self.diaAnteriorP].protein) > self.usuario.peso*40.0: #2900
                maxSodio = int(self.usuario.peso*35.71) #2500
            else:
                maxSodio=int(self.usuario.peso*44.28) #3100
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

class AlergiasConstraint(Constraint[str, Receta]):
    def __init__(self, dia: str, alergias: List) -> None:
        super().__init__([dia])
        self.dia: str = dia
        self.alergias=alergias

    def satisfied(self, assignment: Dict[str, Receta]) -> bool:
        for alergia in self.alergias:
            if assignment[self.dia].alergias.__contains__(alergia): return False
        return True


#Preferencias

class PreferenciasTipoComida(Preference):
    def __init__(self, positivos: List[str], negativos: List[str]) -> None:
        self.positivos = positivos
        self.negativos = negativos

    def funcion_heuristica(self, receta: Receta) -> int:
        valor = 0
        for positivo in self.positivos:
            if receta.contiene.__contains__(positivo):
                valor+=1
        for negativo in self.negativos:
            if receta.contiene.__contains__(negativo):
                valor-=1
        return valor

def calcularDietaSemana(recetas, usuario: Usuario):
    #declarar variables y dominios (Dias y Recetas)
    variables: List[str] = ["LunesP", "LunesS", "MartesP", "MartesS", "MiercolesP", "MiercolesS", "JuevesP", "JuevesS",
     "ViernesP", "ViernesS"]
    domains: Dict[str, List[Receta]] = {}
    for variable in variables:
        domains[variable] = recetas
    
    neighbors: List[tuple] = []
    for i in range (0, len(variables)):
        for j in range (0, len(variables)):
            if(j!=i):
                neighbors.append(tuple((variables[i], variables[j])))

    csp: CSP[str, List[Receta]] = CSP(variables, domains, neighbors)
   
    alergias=usuario.alergias

    #constraints alergias
    for dia in variables:
        csp.add_constraint(AlergiasConstraint(dia, alergias))

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
        csp.add_constraint(CaloriesConstraint(variables[i], variables[i+1], usuario))
        i+=2

    #contraints macros
    i=2
    while i <= len(variables)-2:
        csp.add_constraint(MacroConstraint(variables[i-2], variables[i-1], variables[i], variables[i+1], usuario))
        i+=2
    
    #constraint no repetir
    for i in range(0, len(variables)-1):
        for j in range (i+1, len(variables)):
            csp.add_constraint(NoRepetirComidaConstraint(variables[i], variables[j]))

    #preferencias
    csp.add_preference(PreferenciasTipoComida(usuario.prefiere, usuario.noprefiere))
    #calcular una solucion
    csp.optimizacion()
    solution: Optional[Dict[str, Receta]] = csp.backtracking_search()
    #solution: Optional[Dict[str, Receta]] = csp.backtracking_search_with_arc_consistence()
    
    if solution is None:
        print("No solution found!")
    #else:
        #print(solution)
        #print("{" + "\n".join("{!r}: {!r},".format(k, v) for k, v in solution.items()) + "}")
    return solution