from sklearn.model_selection import GridSearchCV
from sklearn.model_selection import ParameterGrid
from sklearn.model_selection import train_test_split
from sklearn.metrics import roc_auc_score
import parfit.parfit as pf
import util


def gridding(model, X, y):
    # Grid Search
    parameters = {
        'model__alpha': [1e-4, 1e-3, 1e-2, 1e-1, 1e0, 1e1, 1e2, 1e3],  # learning rate
        'model__n_iter': [1000],  # number of epochs
        'model__loss': ['log'],  # logistic regression,
        'model__penalty': ['l2'],
        'model__n_jobs': [-1]
    }
    grid = GridSearchCV(model, parameters, cv=10, scoring="accuracy", n_jobs=1)

    paramGrid = ParameterGrid(parameters)

    X_train, X_test, y_train, y_test = train_test_split(X, y)

    bestModel, bestScore, allModels, allScores = pf.bestFit(model, paramGrid, X_train, y_train, X_test, y_test,
                                                            metric=roc_auc_score,
                                                            bestScore='max',
                                                            scoreLabel='AUC')

    print(bestModel, bestScore)

    return
    # Fitting
    print("Fitting")

    def fit(): grid.fit(X, y)

    util.measure(fit)

    print("Best Score", grid.best_score_)
    print("Best Params", grid.best_params_)
    print(grid.predict(["Fat people should eat less", "really hurts but can be fixed yo pain"]))
    return grid
