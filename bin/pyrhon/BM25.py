from xml.dom import minidom
import math
import numpy as np
from multiprocessing import Pool, cpu_count

class BM25:
    def __init__(self, corpus, tokenizer=None):
        self.corpus_size = len(corpus)
        self.avgdl = 0
        self.doc_freqs = []
        self.idf = {}
        self.doc_len = []
        self.tokenizer = tokenizer

        if tokenizer:
            corpus = self._tokenize_corpus(corpus)

        nd = self._initialize(corpus)
        self._calc_idf(nd)

    def _initialize(self, corpus):
        nd = {}  # word -> number of documents with word
        num_doc = 0
        for document in corpus:
##            print(document)
            self.doc_len.append(len(document))
            num_doc += len(document)

            frequencies = {}
            for word in document:
                if word not in frequencies:
                    frequencies[word] = 0
                frequencies[word] += 1
            self.doc_freqs.append(frequencies)

            for word, freq in frequencies.items():
                try:
                    nd[word]+=1
                except KeyError:
                    nd[word] = 1

        self.avgdl = num_doc / self.corpus_size
        return nd

    def _tokenize_corpus(self, corpus):
        pool = Pool(cpu_count())
        tokenized_corpus = pool.map(self.tokenizer, corpus)
        return tokenized_corpus

    def _calc_idf(self, nd):
        raise NotImplementedError()

    def get_scores(self, query):
        raise NotImplementedError()

    def get_batch_scores(self, query, doc_ids):
        raise NotImplementedError()

    def get_top_n(self, query, documents, n=5):

        assert self.corpus_size == len(documents), "The documents given don't match the index corpus!"

        scores = self.get_scores(query)
        top_n = np.argsort(scores)[::-1][:n]
        return [documents[i] for i in top_n]


class BM25Plus(BM25):
    def __init__(self, corpus, tokenizer=None, k1=2, b=0.75, delta=1):
        # Algorithm specific parameters
        self.k1 = k1
        self.b = b
        self.delta = delta
        super().__init__(corpus, tokenizer)

    def _calc_idf(self, nd):
        for word, freq in nd.items():
            idf = math.log((self.corpus_size + 1) / freq)
            self.idf[word] = idf

    def get_scores(self, query):
        score = np.zeros(self.corpus_size)
        doc_len = np.array(self.doc_len)
        for q in query:
            q_freq = np.array([(doc.get(q) or 0) for doc in self.doc_freqs])
            score += (self.idf.get(q) or 0) * (self.delta + (q_freq * (self.k1 + 1)) /
                                               (self.k1 * (1 - self.b + self.b * doc_len / self.avgdl) + q_freq))
        return score

    def get_batch_scores(self, query, doc_ids):
        """
        Calculate bm25 scores between query and subset of all docs
        """
        assert all(di < len(self.doc_freqs) for di in doc_ids)
        score = np.zeros(len(doc_ids))
        doc_len = np.array(self.doc_len)[doc_ids]
        for q in query:
            q_freq = np.array([(self.doc_freqs[di].get(q) or 0) for di in doc_ids])
            score += (self.idf.get(q) or 0) * (self.delta + (q_freq * (self.k1 + 1)) /
                                               (self.k1 * (1 - self.b + self.b * doc_len / self.avgdl) + q_freq))
        return score.tolist()

##โหลดคำหยุด
f = open("dic/stopwordAndSpc_eng.txt", "r",encoding='utf-8')
stopwords = [x.replace('\n', '') for x in f]

t = open("./data/path/filepath.txt", "r",encoding='utf-8')
path = [x.resplace('\n' '') for x in t]

mydoc = minidom.parse('data/train/traindata.xml')
docs = mydoc.getElementsByTagName('Abstract')
for i in path:
    datatest = minidom.parse(i)
    docstest = datatest.getElementsByTagName('Abstract')

corpus=[]
corpus_test=[]
for elem in docstest:
    if elem.firstChild != None :
        #print(elem.firstChild.data)
        corpus_test.append(elem.firstChild.data)

for elem in docs:
    corpus.append(elem.firstChild.data)

#train
corpus = [doc.lower() for doc in corpus]
tokenized_corpus = [doc.split(" ") for doc in corpus]

#test
corpus_test = [doc.lower() for doc in corpus_test]
tokenized_query = [doc.split(".") for doc in corpus_test]

#word cut
word_cut_stop_corpus = []
for doc in tokenized_corpus:
    doc_word=[]
    for w in doc:
        if w not in stopwords:
            doc_word.append(w)
    word_cut_stop_corpus.append(doc_word)
##print(word_cut_stop_corpus)


word_cut_stop_corpus2 = []
for w in tokenized_query:
        if w not in stopwords:
            word_cut_stop_corpus2.append(w)
            
word_cut_stop_corpus2 = []
for doc in tokenized_query:
    doc_word=[]
    for w in doc: 
        if w not in stopwords:
            doc_word.append(w)
    word_cut_stop_corpus2.append(doc_word)
    

##call bm25
bm25 = BM25Plus(word_cut_stop_corpus)
doc_scores = bm25.get_scores(word_cut_stop_corpus2)

print(doc_scores/(doc_scores+1))
i=1
for doc in word_cut_stop_corpus2:
    doc_scores = bm25.get_scores(doc)
    num = doc_scores/(doc_scores+1)
    print("Doc ", doc_name[i].firstChild.data ,": " , num)
    i+=1
