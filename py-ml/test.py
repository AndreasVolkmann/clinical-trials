import time
import pandas as pd
import numpy as np
from sklearn import metrics
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.feature_extraction.text import TfidfTransformer
from sklearn.model_selection import train_test_split
from sklearn.pipeline import Pipeline
from sklearn.naive_bayes import MultinomialNB
from sklearn.linear_model import SGDClassifier
from sklearn.model_selection import GridSearchCV

data = pd.read_csv("../trials.txt", delimiter="|", quotechar='"')

X = data.Summary
y = data.Label
print(X.shape)
print(y.shape)
print(data.head())


# SGDClassifier : 0.827542937809

pipe = Pipeline([('vect', CountVectorizer(stop_words="english")),
                 ('tfidf', TfidfTransformer(use_idf=True)),
                 ('clf', SGDClassifier(loss='hinge', penalty='l2', n_iter=5, random_state=42)),
                 ])


def gridding(pipe):
    # Grid Search
    parameters = {
    #'vect__ngram_range': [(1, 1), (1, 2)],
    #'tfidf__use_idf': (True, False),
    #'clf__alpha': (1e-2, 1e-3),
    }
    grid = GridSearchCV(pipe, parameters, cv=10, scoring="accuracy", n_jobs=-1)
    # Fitting
    print("Fitting")
    start = time.time()
    grid.fit(X, y)
    end = time.time()
    print("Fitting took", end - start)

    print("Best Score", grid.best_score_)
    print("Best Params", grid.best_params_)

    print(grid.predict(["Fat people should eat less", "really hurts but can be fixed yo pain"]))


def standard(pipe):
    X_train, X_test, y_train, y_test = train_test_split(X, y)
    pipe.fit(X_train, y_train)
    predicted = pipe.predict(X_test)
    print("Score", np.mean(predicted == y_test))
    print(metrics.classification_report(y_test, predicted))
    #print(metrics.confusion_matrix(y_test, predicted))


standard(pipe)

