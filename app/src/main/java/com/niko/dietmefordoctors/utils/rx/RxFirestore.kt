package com.niko.dietmefordoctors.utils.rx

import com.google.firebase.firestore.*
import com.niko.dietmefordoctors.utils.Log
import io.reactivex.Completable
import io.reactivex.Observable


object RxFirestore {

    fun snapDocument(vararg path: String): Observable<DocumentSnapshot> {
        return Observable.create { emitter ->
            val listenerRegistration = FirebaseFirestore.getInstance()
                .document(toFirebasePath(*path))
                .addSnapshotListener { documentSnapshot, e ->

                    if (!emitter.isDisposed) {
                        if (e == null) {
                            emitter.onNext(documentSnapshot!!)
                        } else {
                            emitter.onError(e)
                        }
                    }

                }

            emitter.setCancellable {
                Log.d("удаляем слушатель на документ: " + toFirebasePath(*path))
                listenerRegistration.remove()
            }

        }
    }

    fun snapCollection(vararg path: String): Observable<QuerySnapshot> {
        return Observable.create { emitter ->
            val listenerRegistration = FirebaseFirestore.getInstance()
                .collection(toFirebasePath(*path))
                .addSnapshotListener { querySnapshot, e ->

                    if (!emitter.isDisposed) {
                        if (e == null) {
                            emitter.onNext(querySnapshot!!)
                        } else {
                            emitter.onError(e)
                        }
                    }

                }

            emitter.setCancellable {
                Log.d("удаляем слушатель на коллекцию: " + toFirebasePath(*path))
                listenerRegistration.remove()
            }

        }
    }

    fun snapQuery(query: Query): Observable<QuerySnapshot> {
        return Observable.create { emitter ->
            val listenerRegistration = query.addSnapshotListener { querySnapshot, e ->

                if (!emitter.isDisposed) {
                    if (e == null) {
                        emitter.onNext(querySnapshot!!)
                    } else {
                        emitter.onError(e)
                    }
                }

            }

            emitter.setCancellable {
                Log.d("удаляем слушатель на запрос")
                listenerRegistration.remove()
            }
        }
    }

    operator fun get(collection: String, document: String): Observable<DocumentSnapshot> {
        return Observable.create { observableEmitter ->

            val db = FirebaseFirestore.getInstance()

            val col = db.collection(collection)

            col.document(document)
                .get()
                .addOnSuccessListener { task ->
                    observableEmitter.onNext(task)
                    observableEmitter.onComplete()
                }
                .addOnFailureListener { observableEmitter.onError(it) }

        }
    }

    fun getCollection(vararg path: String): Observable<QuerySnapshot> {
        return Observable.create { observableEmitter ->
            val db = FirebaseFirestore.getInstance()
            db.collection(toFirebasePath(*path))
                .get()
                .addOnSuccessListener { task ->
                    observableEmitter.onNext(task)
                    observableEmitter.onComplete()
                }
                .addOnFailureListener { observableEmitter.onError(it) }
        }
    }

    fun getDocument(vararg path: String): Observable<DocumentSnapshot> {
        return Observable.create { observableEmitter ->
            val db = FirebaseFirestore.getInstance()
            db.document(toFirebasePath(*path))
                .get()
                .addOnSuccessListener { task ->
                    observableEmitter.onNext(task)
                    observableEmitter.onComplete()
                }
                .addOnFailureListener { observableEmitter.onError(it) }
        }
    }

    operator fun get(query: Query): Observable<QuerySnapshot> {
        return Observable.create { observableEmitter ->
            query.get()
                .addOnSuccessListener { task ->
                    observableEmitter.onNext(task)
                    observableEmitter.onComplete()
                }
                .addOnFailureListener { observableEmitter.onError(it) }
        }
    }

    fun updateDocument(data: Map<String, Any>, vararg path: String): Completable {
        return Completable.create { observableEmitter ->
            FirebaseFirestore.getInstance()
                .document(toFirebasePath(*path)).update(data)
                .addOnSuccessListener { aVoid -> observableEmitter.onComplete() }
                .addOnFailureListener { observableEmitter.onError(it) }
        }
    }

    fun setDocument(data: Map<String, Any>, vararg path: String): Completable {
        return Completable.create { observableEmitter ->
            FirebaseFirestore.getInstance()
                .document(toFirebasePath(*path)).set(data)
                .addOnSuccessListener { aVoid -> observableEmitter.onComplete() }
                .addOnFailureListener { observableEmitter.onError(it) }
        }
    }


    operator fun set(
        collection: String, document: String,
        data: Map<String, Any>
    ): Observable<Void> {
        return Observable.create { observableEmitter ->
            FirebaseFirestore.getInstance().collection(collection).document(document).set(data)
                .addOnSuccessListener { aVoid ->
                    observableEmitter.onNext(aVoid)
                    observableEmitter.onComplete()
                }
                .addOnFailureListener { observableEmitter.onError(it) }
        }
    }

    fun add(collection: String, data: Map<String, Any>): Observable<DocumentReference> {
        return Observable.create { observableEmitter ->
            FirebaseFirestore.getInstance().collection(collection).add(data)
                .addOnSuccessListener { documentReference ->
                    observableEmitter.onNext(documentReference)
                    observableEmitter.onComplete()
                }
                .addOnFailureListener { observableEmitter.onError(it) }
        }
    }

    fun addCompletable(collection: String, data: Map<String, Any>): Completable {
        return Completable.create { observableEmitter ->
            FirebaseFirestore.getInstance().collection(collection).add(data)
                .addOnSuccessListener { documentReference -> observableEmitter.onComplete() }
                .addOnFailureListener { observableEmitter.onError(it) }
        }
    }

    operator fun get(documentReference: DocumentReference): Observable<DocumentSnapshot> {
        return Observable.create { observableEmitter ->
            documentReference.get()
                .addOnSuccessListener { task ->
                    observableEmitter.onNext(task)
                    observableEmitter.onComplete()
                }
                .addOnFailureListener { observableEmitter.onError(it) }
        }
    }

    fun toFirebasePath(vararg path: String): String {

        if (path.size == 0) {
            return ""
        }

        val sb = StringBuilder()

        for (i in path.indices) {
            sb.append(path[i])
            if (i != path.size - 1) {
                sb.append("/")
            }
        }

        return sb.toString()
    }
}
