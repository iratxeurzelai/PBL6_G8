from typing import List
from pydantic import BaseModel
class Usuario(BaseModel):
    name : str
    peso : int
    altura : int
    alergias : List