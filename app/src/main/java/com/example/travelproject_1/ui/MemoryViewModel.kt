package com.example.travelproject_1.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.travelproject_1.data.TravelPlan
import com.google.firebase.firestore.FirebaseFirestore

class MemoryViewModel: ViewModel() {

    var memoryList by mutableStateOf(listOf<TravelPlan>())
        private set

    fun getData(){
        var planList = mutableStateListOf<TravelPlan>()

        var db: FirebaseFirestore = FirebaseFirestore.getInstance()

        db.collection("TravelPlans").get()
            .addOnSuccessListener {
                QueryDocumentSnapshot ->

                if(!QueryDocumentSnapshot.isEmpty){
                    var list = QueryDocumentSnapshot.documents

                    for(doc in list){
                        val plan: TravelPlan? = doc.toObject(TravelPlan::class.java)
                        println(plan)
                        planList.add(plan!!)

                    }
                    memoryList = planList.toList()
                    Log.d("GetData","Success")
                }else{
                    //Not obtained
                }
            }
            .addOnFailureListener{
                //Failed
                Log.d("GetData","Failed")
            }



    }
}