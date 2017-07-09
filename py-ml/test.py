import pandas as pd
from sklearn import metrics
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LogisticRegression

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
cv = CountVectorizer(stop_words="english")

X_train_dtm = cv.fit_transform(X_train)
X_test_dtm = cv.transform(X_test)

from sklearn.linear_model import LogisticRegression
model = LogisticRegression(solver='newton-cg', multi_class='ovr')
model.fit(X_train_dtm, y_train)
y_pred_class = model.predict(X_test_dtm)
score = metrics.accuracy_score(y_test, y_pred_class)
print(score)
