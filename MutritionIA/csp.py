from pickletools import optimize
from typing import Generic, TypeVar, Dict, List, Optional
from abc import ABC, abstractmethod

from numpy import empty

V = TypeVar('V') # variable type
D = TypeVar('D') # domain type


# Base class for all constraints
class Constraint(Generic[V, D], ABC):
    # The variables that the constraint is between
    def __init__(self, variables: List[V]) -> None:
        self.variables = variables

    # Must be overridden by subclasses
    @abstractmethod
    def satisfied(self, assignment: Dict[V, D]) -> bool:
        ...

class Preference(Generic[V, D], ABC):
    def __init__(self, positivos: List[str], negativos: List[str]) -> None:
        self.positivos = positivos
        self.negativos = negativos

    @abstractmethod
    def funcion_heuristica(self, receta: D) -> int:
        ...

# A constraint satisfaction problem consists of variables of type V
# that have ranges of values known as domains of type D and constraints
# that determine whether a particular variable's domain selection is valid
class CSP(Generic[V, D]):
    def __init__(self, variables: List[V], domains: Dict[V, List[D]], neighbors: List[tuple]) -> None:
        self.variables: List[V] = variables # variables to be constrained
        self.domains: Dict[V, List[D]] = domains # domain of each variable
        self.constraints: Dict[V, List[Constraint[V, D]]] = {}
        self.preferences: List[Preference] = []
        self.neighbors: List[tuple] = neighbors
        self.curr_domains = None
        for variable in self.variables:
            self.constraints[variable] = []
            if variable not in self.domains:
                raise LookupError("Every variable should have a domain assigned to it.")

    def add_constraint(self, constraint: Constraint[V, D]) -> None:
        for variable in constraint.variables:
            if variable not in self.variables:
                raise LookupError("Variable in constraint not in CSP")
            else:
                self.constraints[variable].append(constraint)
    
    def add_preference(self, preference: Preference) -> None:
        self.preferences.append(preference)

    # Check if the value assignment is consistent by checking all constraints
    # for the given variable against it
    def consistent(self, variable: V, assignment: Dict[V, D]) -> bool:
        for constraint in self.constraints[variable]:
            if not constraint.satisfied(assignment):
                return False
        return True

    def optimizar(self, unassigedList: List[D]) -> D:
        best = unassigedList[0]
        mejorValor = 0
        valor_actual=0
        for unassigned in unassigedList:
            for preference in self.preferences:
                valor_actual+=preference.funcion_heuristica(unassigned)
            if (valor_actual>mejorValor):
                best = unassigned
                mejorValor=valor_actual
            valor_actual=0
        return best

    def optimizacion(self):
        for x in self.domains:
            for preference in self.preferences:
                self.domains[x].sort(key=preference.funcion_heuristica, reverse=True)
                            
    def backtracking_search(self, assignment: Dict[V, D] = {}) -> Optional[Dict[V, D]]:
            # assignment is complete if every variable is assigned (our base case)
            if len(assignment) == len(self.variables):
                return assignment

            # get all variables in the CSP but not in the assignment
            unassigned: List[V] = [v for v in self.variables if v not in assignment]

            # get the every possible domain value of the first unassigned variable
            first: V = unassigned[0]
            for value in self.domains[first]:
                local_assignment = assignment.copy()
                local_assignment[first] = value
                # if we're still consistent, we recurse (continue)
                if self.consistent(first, local_assignment):
                    result: Optional[Dict[V, D]] = self.backtracking_search(local_assignment)
                    # if we didn't find the result, we will end up backtracking
                    if result is not None:
                        return result
            return None

    # def backtracking_search(self, assignment: Dict[V, D] = {}) -> Optional[Dict[V, D]]:
    #     # assignment is complete if every variable is assigned (our base case)
    #     if len(assignment) == len(self.variables):
    #         return assignment

    #     # Get los días no asignados todavia
    #     unassigned: List[V] = [v for v in self.variables if v not in assignment]

    #     # Primer día no asignado
    #     first: V = unassigned[0]

    #     domainsDecreciente=self.domains[first].copy()

    #     while len(domainsDecreciente)!=0:
    #         value = self.optimizar(domainsDecreciente) #receta que mejor encaja con las preferencias
    #         domainsDecreciente.remove(value)
    #         local_assignment = assignment.copy()
    #         local_assignment[first] = value #asignar la receta al día
    #         # if we're still consistent, we recurse (continue)
    #         if self.consistent(first, local_assignment):
    #             result: Optional[Dict[V, D]] = self.backtracking_search(local_assignment)
    #             # if we didn't find the result, we will end up backtracking
    #             if result is not None:
    #                 return result
    #     return None

