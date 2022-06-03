def foo(i):
    if i == 5:
        return 10
    if i==4:
        return 9
    else:
        return 5

lista = [5, 4, 6, 8]

lista.sort(key=foo)

print(lista)