package com.niko.dietmefordoctors.ui.diet

import android.os.Bundle
import com.niko.dietmefordoctors.R
import com.niko.dietmefordoctors.ui.common.activities.BaseActivity

class DietActivity : BaseActivity() {

    private var userId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diet)

        if (intent.extras != null) {
            userId = intent.extras.getString("userId")
        }
    }
}
