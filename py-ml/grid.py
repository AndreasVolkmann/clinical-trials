from sklearn.model_selection import GridSearchCV
import numpy as np
import util


def gridding(model, X, y):
    # Grid Search
    parameters = {
        #'model__n_iter': np.arange(1, 10, 1)
        # 'tfidf__use_idf': (True, False),
    }
    grid = GridSearchCV(model, parameters, cv=10, scoring="accuracy", n_jobs=-1)
    # Fitting
    print("Fitting")

    def fit(): grid.fit(X, y)
    util.measure(fit)

    print("Best Score", grid.best_score_)
    print("Best Params", grid.best_params_)
    print(grid.predict(["Fat people should eat less", "really hurts but can be fixed yo pain"]))
