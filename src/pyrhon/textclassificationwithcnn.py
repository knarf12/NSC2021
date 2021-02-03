# -*- coding: utf-8 -*-

import xml.etree.ElementTree as ET
tree = ET.parse('../../data/train/trainData.xml') # ระบุ pathfile
root = tree.getroot()
data = []
y = []
for child in root:
    for i in child:
        if i.tag == 'Abstract':  # เอาเฉพาะ ข้อมูลที่อยู่ใน tag Abstract
            data.append(i.text)
        if i.tag == 'Class':
            if i.text.lower() == 'diagnosis':
                y.append(0)
            if i.text.lower() == 'reflection':
                y.append(1)
            if i.text.lower() == 'symptom':
                y.append(2)

tree = ET.parse('../../data/dataTest/data_test_1.xml') # ระบุ pathfile
root = tree.getroot()
for child in root:
    for i in child:
        if i.tag == 'Abstract':  # เอาเฉพาะ ข้อมูลที่อยู่ใน tag Abstract
            data.append(i.text)
        if i.tag == 'Class':
            if i.text.lower() == 'diagnosis':
                y.append(0)
            if i.text.lower() == 'reflection':
                y.append(1)
            if i.text.lower() == 'symptom':
                y.append(2)
                

# ฟังก์ชัน ทำ feature 
from textblob import TextBlob
from textblob import Blobber
from textblob import Word
from nltk.corpus import stopwords
from nltk.corpus import wordnet

def get_wordnet_pos(tag):
    if tag.startswith('J'):
        return wordnet.ADJ
    elif tag.startswith('V'):
        return wordnet.VERB
    elif tag.startswith('N'):
        return wordnet.NOUN
    elif tag.startswith('R'):
        return wordnet.ADV
    else:
        return ''
    
def stopwordRemoval(tokens):
    stop_words = stopwords.words('english')
    tokenlist = []
    for w in tokens:
        if w.lower() not in stop_words:
            tokenlist.append(w)
    return tokenlist

def lemma(tokens,text):
    sent = TextBlob(text)
    sent = sent.correct()
    tokenlist = []
    for w,tag in sent.tags:
        if w in tokens:
            tag = get_wordnet_pos(tag)
            if tag != '':
                word = Word(w)
                tokenlist.append((word.lemmatize(tag).lower()))
            else :
                word = Word(w)
                tokenlist.append((word.lemmatize()).lower())
    return tokenlist

def extract_feature(document):
    token = TextBlob(document)
    token = token.correct()
    token = token.words
    token = stopwordRemoval(token)
    token = lemma(token,document)
    return token

feature = []
for doc in data:
    print('Process: ',doc)
    feature.append(extract_feature(doc))
feature

# make model Word2Vec
from gensim.models import word2vec
model = word2vec.Word2Vec(feature, size=300, min_count=1, window=5, iter=50, sg=1)
print(model.wv.vectors)
print(model.wv.vocab.keys())

##model.wv['covid-19'] # ตัวอย่าง print vector ของคำ "covid-19"

MAX_SEQUENCE_LENGTH = max(len(x) for x in feature) # หา sequence ที่ยาวที่สุด
vocab_size  = len(list(model.wv.vocab)) + 1   # หาขนาดของคำศัพท์ที่มี

# ทำ ดัชนีของคำ
from tensorflow.keras.preprocessing.text import Tokenizer
tokenizer = Tokenizer(num_words=100000)
tokenizer.fit_on_texts(feature)
word_index = tokenizer.word_index
word_index

from random import randrange
import numpy as np
X = []
for tokens in feature:
    list_idx = []
    for w in tokens:
        list_idx.append(word_index[w])
    X.append(list_idx)
    
##y = []
##print(len(X))
##for i in range(45):
##    y.append(randrange(2))  # อันนี้ สุ่มกำหนด label ให้แต่ละ document ตามจริงต้องกำหนดมาด้วยมือให้ถูกต้อง
print(X)  #คือ Data
print(y)  #คือ Label


#### มาแก้ข้อมูล train 30, test  ??
from sklearn.model_selection import train_test_split
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.20, random_state=42)

# ทำ pad เติม 0 ให้ครบตามขนาดของ MAX_SEQUENCE_LENGTH ในกรณีที่ data ขุดใดๆ ที่ขนาดน้อยกว่า MAX_SEQUENCE_LENGTH
from keras.preprocessing.sequence import pad_sequences
X_train = pad_sequences(X_train, padding='post', maxlen=MAX_SEQUENCE_LENGTH)
X_test = pad_sequences(X_test, padding='post', maxlen=MAX_SEQUENCE_LENGTH)

X_train = np.array(X_train)
X_test = np.array(X_test)
y_train = np.array(y_train)
y_test = np.array(y_test)

# ทำ embedding_matrix เพื่อเป็น weigth ของคำ จาก word2vec ที่เรียนรู้มาจากด้านบน
import numpy as np
embedding_matrix = np.zeros((len(word_index) + 1, 300))
for word, i in word_index.items():
   embedding_matrix[i] = model.wv[word]

embedding_matrix.shape[1]

import matplotlib.pyplot as plt
plt.style.use('ggplot')

def plot_history(history):
    acc = history.history['accuracy']
    val_acc = history.history['val_accuracy']
    loss = history.history['loss']
    val_loss = history.history['val_loss']
    x = range(1, len(acc) + 1)

    plt.figure(figsize=(12, 5))
    plt.subplot(1, 2, 1)
    plt.plot(x, acc, 'b', label='Training acc')
    plt.plot(x, val_acc, 'r', label='Validation acc')
    plt.title('Training and validation accuracy')
    plt.legend()
    plt.subplot(1, 2, 2)
    plt.plot(x, loss, 'b', label='Training loss')
    plt.plot(x, val_loss, 'r', label='Validation loss')
    plt.title('Training and validation loss')
    plt.legend()

from keras.models import Sequential
from keras import layers
from keras.layers import Dense,Flatten,Conv1D,add, Embedding, Add, GlobalMaxPool1D

embedding_dim = embedding_matrix.shape[1]
# Use CNN model from Ramay 
model_cnn = Sequential()
model_cnn.add(layers.Embedding(input_dim=vocab_size, 
                           output_dim=embedding_dim, 
                           input_length=MAX_SEQUENCE_LENGTH,
                           weights=[embedding_matrix],
                           trainable=True))
model_cnn.add(Conv1D(128,1, activation='tanh'))
model_cnn.add(Flatten())
model_cnn.add(layers.Dense(3, activation='softmax'))
model_cnn.compile(optimizer='adam', loss='sparse_categorical_crossentropy', metrics=['accuracy'])
model_cnn.summary()


history = model_cnn.fit(X_train, y_train,
                    epochs=10,
                    verbose=False,
                    validation_data=(X_test, y_test),
                    batch_size=10)
loss, accuracy = model_cnn.evaluate(X_train, y_train, verbose=True)
print("Training Accuracy: {:.4f}".format(accuracy))
loss, accuracy = model_cnn.evaluate(X_test, y_test, verbose=True)
print("Testing Accuracy:  {:.4f}".format(accuracy))
plot_history(history)

# Use Deep Learning 
embedding_dim = embedding_matrix.shape[1]
 
model_deep = Sequential()
model_deep.add(layers.Embedding(input_dim=vocab_size, 
                           output_dim=embedding_dim, 
                           input_length=MAX_SEQUENCE_LENGTH,
                           weights=[embedding_matrix],
                           trainable=True))
model_deep.add(layers.GlobalMaxPool1D())
model_deep.add(layers.Dense(128, activation='relu'))
model_deep.add(layers.Dense(3, activation='softmax'))
model_deep.compile(optimizer='adam', loss='sparse_categorical_crossentropy', metrics=['accuracy'])
model_deep.summary()

history = model_deep.fit(X_train, y_train,
                    epochs=10,
                    verbose=False,
                    validation_data=(X_test, y_test),
                    batch_size=10)
loss, accuracy = model_deep.evaluate(X_train, y_train, verbose=True) 
print("Training Accuracy: {:.4f}".format(accuracy))
loss, accuracy = model_deep.evaluate(X_test, y_test, verbose=True)
print("Testing Accuracy:  {:.4f}".format(accuracy))
plot_history(history)

y_result = model_deep.predict(X_test, batch_size=10, verbose=0)
##print(y_result)

f = open("../../Deep/KNNresult.txt", "w")
for i in y_result:
    s = ""
    for cc in i:
        s = s +str(cc)+ " "
    f.write(s)
    f.write("\n")
    print(s)
f.close()


##from sklearn.metrics import accuracy_score,precision_score,recall_score,f1_score,roc_auc_score,matthews_corrcoef,classification_report
##from sklearn.metrics import confusion_matrix

##y_result = model_deep.predict(X_test, batch_size=10, verbose=0)
##y_pred_bool = np.argmax(y_result, axis=2)
### print(classification_report(y_test, y_pred_bool))
##acc = accuracy_score(y_test, y_pred_bool)
### print('Accuracy: %f' % acc)
##
##pre = precision_score(y_test, y_pred_bool,average='macro')
### print('Precision: %f' % pre)
##
##rec = recall_score(y_test, y_pred_bool, average='macro')
### print('Recall: %f' % rec)
##
##f = f1_score(y_test, y_pred_bool,average='macro')
### print('F1 score: %f' % f)

### ROC AUC
##roc_auc = roc_auc_score(y_test, y_pred_bool ,multi_class='ovr')
###print('ROC AUC: %f' % roc_auc)
##
### ROC MCC
##m = matthews_corrcoef(y_test, y_pred_bool)
###print('MCC: %f' % mcc)
##
### # confusion matrix
##matrix = confusion_matrix(y_test, y_pred_bool)
##print(matrix)

##print("Acc: %.3f" % acc)
##print("Recall: %.3f " % rec)
##print("Precision: %.3f" % pre)
##print("F1: %.3f" % f)
##print("AUC: %.3f" % roc_auc)
##print("MCC: %.3f" % m)

