from gensim.models import Word2Vec

model = Word2Vec.load('D:\Dev\data\jp\word2vec-genism-model\word2vec.gensim.model')
model.init_sims(replace=True)

res = model.most_similar('日')
print(res)

model.wv.save_word2vec_format("model.bin.gz", binary=True)

# FastText.load_fasttext_format('D:\Dev\data\jp\wiki.ja.vec')
