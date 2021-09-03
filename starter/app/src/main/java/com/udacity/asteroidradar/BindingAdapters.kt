package com.udacity.asteroidradar

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.main.MainAdapter

@BindingAdapter("imgOfTheDay")
fun bindImageOfTheDay(imageView: ImageView,img_url:String?){
    img_url?.let{
        Picasso.get().load(img_url).placeholder(R.drawable.loading_image).into(imageView)
    }
}
@BindingAdapter("contentImg")
fun bindContentDescImg(imageView: ImageView,title:String?){
    if (title!=null){
        imageView.contentDescription = String.format(imageView.context.getString(R.string.nasa_picture_of_day_content_description_format), title)
    }else{
        imageView.contentDescription = imageView.context.resources.getString(R.string.this_is_nasa_s_picture_of_day_showing_nothing_yet)
    }

}

@BindingAdapter("loadingImg")
fun bindLoadingImg(imageView: ImageView,data:List<Asteroid>?){
    if (data==null){
        imageView.visibility = View.VISIBLE
        imageView.contentDescription = imageView.context.resources.getString(R.string.loading_image)
    }

}

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
        imageView.contentDescription = imageView.context.resources.getString(R.string.potentially_hazardous_asteroid_image)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
        imageView.contentDescription = imageView.context.resources.getString(R.string.not_hazardous_asteroid_image)
    }
}


@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
        imageView.contentDescription="Hazardous Asteroid Image"
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
        imageView.contentDescription="Not Hazardous Asteroid Image"
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

@BindingAdapter("listAsteroid")
fun bindRecycerView(recyclerView: RecyclerView,data:List<Asteroid>?){
    val adapter =recyclerView.adapter as MainAdapter
    adapter.submitList(data)
}
