from typing import List
from pydantic import BaseModel
class Usuario(BaseModel):
    id : int
    peso : int
    altura : int
    alergias : List
    prefiere : List
    noprefiere : List