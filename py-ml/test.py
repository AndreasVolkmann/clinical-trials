import numpy as np
import pandas as pd
from sklearn import metrics
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.feature_extraction.text import TfidfTransformer
from sklearn.linear_model import SGDClassifier
from sklearn.model_selection import train_test_split
from sklearn.pipeline import Pipeline

import util

filename = "trials_combined_text.psv"
data = pd.read_csv("../" + filename, delimiter="|", quotechar='"')

X = data.Text
y = data.Label


# print(X.shape)
# print(y.shape)
# print(data.head(10))


def make_pipe(model):
    return Pipeline([
        ('vect', CountVectorizer(stop_words="english")),
        ('tfidf', TfidfTransformer(use_idf=True)),
        ('model', model)
    ])


def write(file_name, content):
    file = open(file_name, "w")
    file.write(content)
    file.close()


def read(file_name):
    file = open(file_name, "r")
    content = file.read()
    file.close()
    return content


def write_results(score, report):
    old_content = read("results.txt")
    content = "Score: {}\n".format(score) + report
    # print(old_content)
    # print(content)
    write("results.txt", content)
    write("old_res.txt", old_content)


def standard(pipe):
    print("Pipeline:", type(pipe.named_steps["model"]).__name__)
    X_train, X_test, y_train, y_test = train_test_split(X, y)

    def fit(): pipe.fit(X_train, y_train)

    util.measure(fit)
    predicted = pipe.predict(X_test)
    score = np.mean(predicted == y_test)
    report = metrics.classification_report(y_test, predicted)
    write_results(score, report)
    print(score)
    # print(metrics.confusion_matrix(y_test, predicted))


sgd = SGDClassifier(loss='hinge', penalty='l2', max_iter=8, random_state=42)

classifier = sgd
pipe = make_pipe(classifier)
standard(pipe)


# standard(pipe)
# SGDClassifier(loss='hinge', penalty='l2')


def getModel(alpha, n_iter, loss, penalty, n_jobs):
    return make_pipe(SGDClassifier(loss=loss, penalty=penalty, alpha=alpha, n_jobs=n_jobs, n_iter=n_iter))

# if __name__ == "__main__": grid.gridding(pipe, X, y)
