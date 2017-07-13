import time
import pandas as pd
from sklearn import metrics
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.model_selection import train_test_split
from sklearn.naive_bayes import MultinomialNB

data = pd.read_csv("../trials.txt", delimiter="|", quotechar='"')

X = data.Summary
y = data.Label
print(X.shape)
print(y.shape)
print(data.head())

print(X.dtypes)

# Train Test Split
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.25, random_state=3)

# Count Vectorizer
print("CountVectorizer")
cv = CountVectorizer(stop_words="english")

X_train_dtm = cv.fit_transform(X_train)
X_test_dtm = cv.transform(X_test)

# Multi
print("Multi")
# Fitting
print("Fitting")
start = time.time()
model = MultinomialNB(alpha=0.90)  # TODO grid search
model.fit(X_train_dtm.toarray(), y_train)
end = time.time()
print("Fitting took", end - start)

# Predicting
print("Predicting")
start = time.time()
y_pred_class = model.predict(X_test_dtm.toarray())
end = time.time()
print("Predicting took", end - start)
score = metrics.accuracy_score(y_test, y_pred_class)
print(score)
