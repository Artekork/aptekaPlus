package com.arekok.ink.apteka

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.arekok.ink.apteka.databinding.ActivityMainBinding
import com.arekok.ink.apteka.drawerbar.MyProfile
import com.arekok.ink.apteka.medicineNew.MedicineAdapter
import com.arekok.ink.apteka.medicineNew.MedicineData
import com.arekok.ink.apteka.pharmacyNew.PharmacyAdapter
import com.arekok.ink.apteka.pharmacyNew.PharmacyData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.gms.maps.model.CameraPosition
import com.arekok.ink.apteka.pharmacyNew.DataUpdater
import android.text.TextWatcher
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.arekok.ink.apteka.basket.BasketAdapter
import com.arekok.ink.apteka.basket.BasketData
import com.arekok.ink.apteka.medicineNew.MedicineInfo
import com.firebase.ui.database.FirebaseRecyclerAdapter
import kotlinx.android.synthetic.main.activity_medicine_info.view.*
import kotlinx.android.synthetic.main.medicine_item.view.*
import kotlinx.android.synthetic.main.pharmacy_item.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), OnMapReadyCallback, DataUpdater {
    // ResV1
    lateinit var mDataBase:DatabaseReference
    private lateinit var medicineList:ArrayList<MedicineData>
    private lateinit var mAdapter:MedicineAdapter

    lateinit var mSearchText : EditText
    lateinit var mRecyclerView : RecyclerView
    lateinit var FirebaseRecyclerAdapterM : FirebaseRecyclerAdapter<MedicineData, MedViewHolder>

    //ResV2
    lateinit var pDatabase:DatabaseReference
    private lateinit var pharmacyList:ArrayList<PharmacyData>
    private lateinit var pAdapter:PharmacyAdapter


    lateinit var pSearchText : EditText
    lateinit var pRecyclerView : RecyclerView
    lateinit var FirebaseRecyclerAdapterP : FirebaseRecyclerAdapter<PharmacyData, PharmViewHolder>

    //ResV3
    lateinit var fDataBase:DatabaseReference
    private lateinit var favouriteList:ArrayList<MedicineData>
    private lateinit var fAdapter:MedicineAdapter

    //ResV4
    lateinit var bDataBase:DatabaseReference
    private lateinit var basketList:ArrayList<BasketData>
    private lateinit var bAdapter: BasketAdapter

    ////////////////

    // SEARCH //
    private lateinit var newPharmacyList:ArrayList<PharmacyData>
    private lateinit var tempPharmacyList:ArrayList<PharmacyData>
    ////////////


    private lateinit var database: DatabaseReference
    private lateinit var database1: DatabaseReference

    private lateinit var mMap: GoogleMap

    lateinit var auth: FirebaseAuth

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nameUser = intent.getStringExtra("user_login").toString()

        val databaseUsers = FirebaseDatabase.getInstance().getReference("users")
        databaseUsers.child(nameUser).get().addOnSuccessListener {
            val photoUrl1:String = it.child("photourl").value.toString()
            if (photoUrl1 != "" && photoUrl1 != "null") {
                Picasso.get().load(photoUrl1).into(binding.userImg)
                Picasso.get().load(photoUrl1).into(binding.imageView5)
            }
            val nameB = it.child("name").value
            val surnameB = it.child("surname").value
            val loginB = it.child("mail").value
            if ((nameB != null)&&(nameB!="null")){
                binding.nameDraw.visibility = View.VISIBLE
                binding.nameDraw.text = nameB.toString()
            }
            if ((surnameB != null)&&(surnameB!="null")){
                binding.surnameDraw.visibility = View.VISIBLE
                binding.surnameDraw.text = surnameB.toString()
            }
            if ((loginB != null)&&(loginB!="null")){
                binding.mailDraw.visibility = View.VISIBLE
                binding.mailDraw.text = loginB.toString()
            }
        }
        binding.loginMain.text = nameUser
        binding.Nav.selectedItemId=R.id.pharmacy
        binding.main.visibility = View.VISIBLE
        binding.Nav.selectedItemId=R.id.pharmacy

        setData(nameUser)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.maps) as SupportMapFragment
        mapFragment.getMapAsync(this)

        database1 = FirebaseDatabase.getInstance().getReference("users")

        ////////// RecV1 //////////

        medicineList = ArrayList()
        mAdapter = MedicineAdapter(this, medicineList, nameUser)
        ResV1.layoutManager = LinearLayoutManager(this)
        ResV1.setHasFixedSize(true)
        init1()

        ////////// RecV2 //////////

        pharmacyList = ArrayList()
        pAdapter = PharmacyAdapter(this, pharmacyList,this)
        ResV2.layoutManager = LinearLayoutManager(this)
        ResV2.setHasFixedSize(true)
        init2()
        ////////// RecV3 //////////

        favouriteList = ArrayList()
        fAdapter = MedicineAdapter(this, favouriteList, nameUser)
        ResV3.layoutManager = LinearLayoutManager(this)
        ResV3.setHasFixedSize(true)
        init3(nameUser)

        ////////// RecV4 //////////

        basketList = ArrayList()
        bAdapter = BasketAdapter(this, basketList, nameUser)
        ResV4.layoutManager = LinearLayoutManager(this)
        ResV4.setHasFixedSize(true)
        init4(nameUser)

        ////////// RecV1_2 //////////

        mSearchText = binding.serchMedText
        mRecyclerView = binding.ResV1
        mDataBase = FirebaseDatabase.getInstance().getReference("medicine")
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mSearchText.addTextChangedListener(object  : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (mSearchText.text.toString() != "") {
                    val searchTextM = mSearchText.text.toString().trim()
                    loadFirebaseDataM(searchTextM, nameUser)
                    medicineList.clear()
                } else {
                    init1()
                }
            }
        } )
                ////////// RecV2_2 //////////
                pSearchText = binding.serchText
                pRecyclerView = binding.ResV2
                pDatabase = FirebaseDatabase.getInstance().getReference("apteki")
                pRecyclerView.setHasFixedSize(true)
                pRecyclerView.layoutManager = LinearLayoutManager(this)
                pSearchText.addTextChangedListener(object  : TextWatcher {
                    override fun afterTextChanged(p0: Editable?) {

                    }

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        if (pSearchText.text.toString() != "") {
                            val searchText = pSearchText.text.toString().trim()
                            loadFirebaseDataP(searchText)
                            pharmacyList.clear()
                        } else {
                            init2()
                        }
                    }
                } )

        //////////////////////////////////////////////////////

        var ex = 0
        binding.exitAcc.setOnClickListener{

            if (ex==1) OnClickSignOut()
            else Toast.makeText(this, "Вы уверены?", Toast.LENGTH_SHORT).show()
            ex++
        }
        //////////////////////////////////////////////////////

        binding.lt1.visibility = View.GONE
        binding.lt2.visibility = View.VISIBLE
        binding.lt3.visibility = View.GONE
        binding.lt4.visibility = View.GONE
        binding.Nav.setOnItemSelectedListener {
                item -> when (item.itemId){
                R.id.medicine -> {
                    clearData()
                    binding.lt1.visibility = View.VISIBLE
                    binding.lt2.visibility = View.GONE
                    binding.lt3.visibility = View.GONE
                    binding.lt4.visibility = View.GONE

                    init1()
                }
                R.id.pharmacy -> {
                    clearData()
                    binding.lt1.visibility = View.GONE
                    binding.lt2.visibility = View.VISIBLE
                    binding.lt3.visibility = View.GONE
                    binding.lt4.visibility = View.GONE

                    init2()
                    }
                R.id.favorite -> {
                    clearData()
                    binding.lt1.visibility = View.GONE
                    binding.lt2.visibility = View.GONE
                    binding.lt3.visibility = View.VISIBLE
                    binding.lt4.visibility = View.GONE
                    init3(nameUser)
                }
                R.id.basket -> {
                    clearData()
                    binding.lt1.visibility = View.GONE
                    binding.lt2.visibility = View.GONE
                    binding.lt3.visibility = View.GONE
                    binding.lt4.visibility = View.VISIBLE

                    init4(nameUser)
                }
            }
            true
        }
        binding.card.setOnClickListener{
            binding.drawer.openDrawer(GravityCompat.END)

        }
    }
    private fun loadFirebaseDataM(searchText : String, nameUser: String) =
        if(searchText.isEmpty()){
            FirebaseRecyclerAdapterM.cleanup()
            mRecyclerView.adapter = FirebaseRecyclerAdapterM
        }else {
            val firebaseSearchQuery = mDataBase.orderByChild("nameM").startAt(searchText).endAt(searchText + "\uf8ff")
            FirebaseRecyclerAdapterM = object : FirebaseRecyclerAdapter<MedicineData, MedViewHolder>(
                MedicineData::class.java,
                R.layout.medicine_item,
                MedViewHolder::class.java,
                firebaseSearchQuery
            ) {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun populateViewHolder(viewHolder: MedViewHolder, model: MedicineData?, position: Int) {
                    viewHolder.mview.name.text = model?.nameM
                    viewHolder.mview.type.text = model?.type
                    viewHolder.mview.kolvo.text = model?.price
                    viewHolder.mview.setOnClickListener{
                        val mIntent = Intent(this@MainActivity, MedicineInfo::class.java)
                        mIntent.putExtra("img",model?.img)
                        mIntent.putExtra("nameM",model?.nameM)
                        mIntent.putExtra("srok",model?.srok)
                        mIntent.putExtra("how",model?.how)
                        mIntent.putExtra("structure",model?.structure)
                        this@MainActivity.startActivity(mIntent)
                    }
                    database = FirebaseDatabase.getInstance().getReference("users")
                    database.child(nameUser).get().addOnSuccessListener {
                        val bs = it.child("basket").child(model?.nameM.toString()).value

                        if ((bs == "0")||(bs == null)||(bs == "null")){
                            viewHolder.mview.Basket.setImageResource(R.drawable.nobasket)
                        } else{
                            viewHolder.mview.Basket.setImageResource(R.drawable.onbasket)
                        }
                    }
                    viewHolder.mview.Basket.setOnClickListener {
                        database = FirebaseDatabase.getInstance().getReference("users")
                        database.child(nameUser).get().addOnSuccessListener {
                            val bs = it.child("basket").child(model?.nameM.toString()).value

                            if ((bs == "0")||(bs == null)||(bs == "null")){
                                viewHolder.mview.Basket.setImageResource(R.drawable.onbasket)
                                database.child(nameUser).child("basket").child(model?.nameM.toString()).setValue("1")
                            } else{
                                viewHolder.mview.Basket.setImageResource(R.drawable.nobasket)
                                database.child(nameUser).child("basket").child(model?.nameM.toString()).setValue("0")
                            }
                        }
                    }
                    database.child(nameUser).get().addOnSuccessListener {
                        val fv = it.child("favourite").child(model?.nameM.toString()).value
                        if ((fv == "0")||(fv == null)||(fv == "null")){
                            viewHolder.mview.Favourite.setImageResource(R.drawable.star_notselected)
                        } else{
                            viewHolder.mview.Favourite.setImageResource(R.drawable.star_selected)
                        }
                    }
                    viewHolder.mview.Favourite.setOnClickListener {
                        database = FirebaseDatabase.getInstance().getReference("users")
                        database.child(nameUser).get().addOnSuccessListener {
                            val fv = it.child("favourite").child(model?.nameM.toString()).value
                            if ((fv == "0")||(fv == null)||(fv == "null")){
                                viewHolder.mview.Favourite.setImageResource(R.drawable.star_selected)
                                database.child(nameUser).child("favourite").child(model?.nameM.toString()).setValue("1")
                            } else{
                                viewHolder.mview.Favourite.setImageResource(R.drawable.star_notselected)
                                database.child(nameUser).child("favourite").child(model?.nameM.toString()).setValue("0")
                            }
                        }
                    }
                }
            }
            mRecyclerView.adapter = FirebaseRecyclerAdapterM
        }
    private fun loadFirebaseDataP(searchText : String) {
        if(searchText.isEmpty()){
            FirebaseRecyclerAdapterP.cleanup()
            pRecyclerView.adapter = FirebaseRecyclerAdapterP
        }else {
            val firebaseSearchQuery = pDatabase.orderByChild("nameP").startAt(searchText).endAt(searchText + "\uf8ff")

            FirebaseRecyclerAdapterP = object : FirebaseRecyclerAdapter<PharmacyData, PharmViewHolder>(
                PharmacyData::class.java,
                R.layout.pharmacy_item,
                PharmViewHolder::class.java,
                firebaseSearchQuery
            ) {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun populateViewHolder(viewHolder: PharmViewHolder, model: PharmacyData?, position: Int) {
                    viewHolder.pview.NamePharm.setText(model?.nameP)
                    viewHolder.pview.LocatePharm.setText(model?.location)
                    viewHolder.pview.WorkPharm.setText(model?.work)
                    val currentDateTime = LocalDateTime.now()
                    val data = currentDateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                    if (data < model?.end.toString() && data >  model?.start.toString()){
                        viewHolder.pview.imageStatus.setImageResource(R.drawable.open)
                    } else {
                        viewHolder.pview.imageStatus.setImageResource(R.drawable.close)
                    }
                    viewHolder.pview.setOnClickListener{
                        addMarkFun(model?.loc1!!,model?.loc2!!, model?.nameP!!, model?.work!!)
                    }
                }
            }
            pRecyclerView.adapter = FirebaseRecyclerAdapterP
        }
    }
    // // SEARCH// //
    class PharmViewHolder(var pview : View) : RecyclerView.ViewHolder(pview) {
    }
    class MedViewHolder(var mview : View) : RecyclerView.ViewHolder(mview) {
    }

    private fun init1(){
        mDataBase = FirebaseDatabase.getInstance().getReference("medicine")
        mDataBase.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (medicineSnapshot in snapshot.children){
                        val medicine = medicineSnapshot.getValue(MedicineData::class.java)
                        medicineList.add(medicine!!)
                    }
                    ResV1.adapter = mAdapter
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity,
                    error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun init2(){
        pDatabase = FirebaseDatabase.getInstance().getReference("apteki")
        pDatabase.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (pharmacySnapshot in snapshot.children){
                        val pharmacyN = pharmacySnapshot.getValue(PharmacyData::class.java)
                        pharmacyList.add(pharmacyN!!)
                    }
                    ResV2.adapter = pAdapter
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity,
                    error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun init3(userName:String) {

        var listOfFavourite = ArrayList<String>()

        mDataBase = FirebaseDatabase.getInstance().getReference("users")
        mDataBase.child(userName).child("favourite")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (favouriteSnapshot in snapshot.children) {
                            if (favouriteSnapshot.value.toString() == "1"){
                                listOfFavourite.add(favouriteSnapshot.key.toString())
                            }
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@MainActivity,
                        error.message, Toast.LENGTH_SHORT
                    ).show()
                }
            })

        fDataBase = FirebaseDatabase.getInstance().getReference("medicine")
        fDataBase.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (favouriteSnapshot in snapshot.children){
                       if (favouriteSnapshot.key.toString() in listOfFavourite) {
                            val favourite = favouriteSnapshot.getValue(MedicineData::class.java)
                            favouriteList.add(favourite!!)
                       }
                    }
                    ResV3.adapter = fAdapter
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity,
                    error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun init4(userName:String) {

        var listOfBasket = ArrayList<String>()

        bDataBase = FirebaseDatabase.getInstance().getReference("users")
        bDataBase.child(userName).child("basket")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var ii = 0
                        for (basketSnapshot in snapshot.children) {
                            if (basketSnapshot.value.toString() != "0"){
                                listOfBasket.add(basketSnapshot.key.toString())
                                ii++
                            }
                        }
                        if (ii==0){
                            binding.button.visibility = View.GONE
                        } else binding.button.visibility = View.VISIBLE
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@MainActivity,
                        error.message, Toast.LENGTH_SHORT
                    ).show()
                }
            })

        bDataBase = FirebaseDatabase.getInstance().getReference("medicine")
        bDataBase.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (basketSnapshot in snapshot.children){
                        if (basketSnapshot.key.toString() in listOfBasket) {
                            val basket = basketSnapshot.getValue(BasketData::class.java)
                            basketList.add(basket!!)
                        }
                    }
                    ResV4.adapter = bAdapter
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity,
                    error.message, Toast.LENGTH_SHORT).show()
            }
        })

    }
    @SuppressLint("SetTextI18n")
    fun clearData() {
        binding.ResV1.adapter = null
        medicineList.clear()
        binding.serchMedText.text.clear()
        binding.ResV2.adapter = null
        pharmacyList.clear()
        binding.serchText.text.clear()
        binding.ResV3.adapter = null
        favouriteList.clear()
        basketList.clear()
    }

    private fun setData(usName: String) {
        database = FirebaseDatabase.getInstance().getReference("users")
        database.child(usName).get().addOnSuccessListener {
            val name = it.child("name").value
            val surname = it.child("surname").value
            val mail = it.child("mail").value
            if (name.toString() != ""  && surname.toString() != "" && mail.toString() != ""){
                if (name.toString() != "null"  && surname.toString() != "null" && mail.toString() != "null"){
                    binding.nameDraw.text= name.toString()
                    binding.surnameDraw.text=surname.toString()
                    binding.mailDraw.text=mail.toString()
                }
            }
        }
    }
    fun onClickGoProfile(view: View){
        val perenos3 = Intent(this, MyProfile::class.java)
        perenos3.putExtra("user_login", binding.loginMain.text.toString())
        startActivity(perenos3)
    }

    fun OnClickSignOut(){
        val perenos = Intent(this, Autorization::class.java)
        startActivity(perenos)
        finish()
    }

    fun goToOrder(view: View){
        val perenos = Intent(this, Order::class.java)
        perenos.putExtra("user_login", binding.loginMain.text.toString())
        startActivity(perenos)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val alfa = LatLng(53.89740777568895, 30.3380068506267)
        mMap.addMarker(MarkerOptions().position(alfa).title("Альфа аптека").snippet("09:00-21:00"))?.showInfoWindow()
        val cameraPosition = CameraPosition.Builder()
            .target(alfa)
            .zoom(10f)
            .build()
        val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
        mMap.animateCamera(cameraUpdate)
    }
    internal fun addMark(loc1: Double, loc2: Double, name: String, work: String) {
        val iskamed = LatLng(loc1, loc2)
        mMap.addMarker(MarkerOptions().position(iskamed).title(name).snippet(work))?.showInfoWindow()
    }
    override fun addMarkFun(loc1: Double, loc2: Double, nameP:String, work: String) {
        mMap.clear()
        val iskamed = LatLng(loc1, loc2)
        mMap.addMarker(MarkerOptions().position(iskamed).title(nameP).snippet(work))?.showInfoWindow()
        val cameraPosition = CameraPosition.Builder()
            .target(iskamed)
            .zoom(16f)
            .build()
        val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
        mMap.animateCamera(cameraUpdate)
    }

}