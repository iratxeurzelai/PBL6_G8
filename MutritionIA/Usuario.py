from typing import List
from pydantic import BaseModel
class Usuario(BaseModel):
    name : str
    peso : int
    altura : float
    alergias : List