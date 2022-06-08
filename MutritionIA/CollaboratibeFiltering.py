import pandas as pd
import difflib
import numpy as np
import pickle
from surprise import NormalPredictor
from surprise import Dataset
from surprise import Reader
from surprise import KNNBasic
from surprise.model_selection import cross_validate


def getTestSetUnico(trainSet):
    anti_testset_user = []
    targetUser = 0
    fillValue = trainSet.global_mean
    user_item_ratings = trainSet.ur[targetUser]
    user_items = [item for (item, _) in (user_item_ratings)]
    user_items
    ratings = trainSet.all_ratings()
    for iid in trainSet.all_items():
        if(iid not in user_items):
            anti_testset_user.append((trainSet.to_raw_uid(
                targetUser), trainSet.to_raw_iid(iid), fillValue))
    return anti_testset_user


def getFullTestSet(trainSet):
    return trainSet.build_anti_testset()


def getRecomendaciones2():
    # cargar datos
    recetas = pd.read_csv('recetas_procesadas.csv')
    user_recipe_likes = pd.read_csv('userlikes.csv')
    user_recipe_likes.drop(columns=['Unnamed: 0'], inplace=True)

    # preparar para Surprise
    reader = Reader(rating_scale=(0, 1))
    data = Dataset.load_from_df(
        user_recipe_likes[['userid', 'recetaid', 'like']], reader)

    # crear trainset
    trainSet = data.build_full_trainset()

    anti_testset_user = getTestSetUnico(trainSet)

    # KNNeighbors
    sim_options = {'name': 'cosine',
                   'user_based': True  # False: similitud entre recetas | True: entre usuarios
                   }
    predictorKN = KNNBasic(sim_options=sim_options)

    predictorKN.fit(trainSet)

    # predecir
    predictions = predictorKN.test(anti_testset_user)

    # top 10 recetas
    pred = pd.DataFrame(predictions)
    pred.sort_values(by=['est'], inplace=True, ascending=False)
    recipe_list = pred.head(5)['iid'].to_list()
    recetas.loc[recipe_list]

    estimaciones = []
    for i in range(0, len(recetas)):
        estimaciones.append((i, predictorKN.predict(1, i)[3]))

    estimaciones.sort(key=lambda tup: tup[1], reverse=True)

    return(estimaciones[:10])
