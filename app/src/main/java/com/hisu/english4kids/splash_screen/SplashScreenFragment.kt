package com.hisu.english4kids.splash_screen

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gdacciaro.iOSDialog.iOSDialogBuilder
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.hisu.english4kids.R
import com.hisu.english4kids.data.CONTENT_TYPE_JSON
import com.hisu.english4kids.data.STATUS_OK
import com.hisu.english4kids.data.network.API
import com.hisu.english4kids.data.network.response_model.AuthResponseModel
import com.hisu.english4kids.data.network.response_model.Player
import com.hisu.english4kids.databinding.FragmentSplashScreenBinding
import com.hisu.english4kids.ui.auth.CheckOTPFragment
import com.hisu.english4kids.utils.local.LocalDataManager
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashScreenFragment : Fragment() {

    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var localDataManager: LocalDataManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        localDataManager = LocalDataManager()
        localDataManager.init(requireContext())

        findNavController().navigate(R.id.splash_to_home)

//        val userLoginState = getLoginStatus()
//        val currentUser = Gson().fromJson(localDataManager.getUserInfo(), Player::class.java)
//
//        if(userLoginState) {
//            Handler(requireContext().mainLooper).postDelayed({
//                val jsonObject = JsonObject()
//                jsonObject.addProperty("phone", currentUser.phone)
//
//                val loginBodyRequest = RequestBody.create(MediaType.parse(CONTENT_TYPE_JSON), jsonObject.toString())
//
//                API.apiService.authLogin(loginBodyRequest).enqueue(handleLoginCallback)
//            }, 5 * 1000)
//        } else {
//            Handler(requireContext().mainLooper).postDelayed({
//                findNavController().navigate(R.id.splash_to_regis)
//            }, 3 * 1000)
//        }
    }

    private val handleLoginCallback = object : Callback<AuthResponseModel> {
        override fun onResponse(
            call: Call<AuthResponseModel>,
            response: Response<AuthResponseModel>
        ) {

            if (response.isSuccessful && response.code() == STATUS_OK) {
                response.body()?.apply {
                    this.data?.apply {

                        val playerInfoJson = Gson().toJson(this.player)

                        localDataManager.setUserLoinState(true)
                        localDataManager.setUserInfo(playerInfoJson)

                        findNavController().navigate(R.id.splash_to_home)
                    }
                }
            } else {
                response.errorBody()?.apply {
                    requireActivity().runOnUiThread {
                        iOSDialogBuilder(requireContext())
                            .setTitle(requireContext().getString(R.string.request_err))
                            .setSubtitle(requireContext().getString(R.string.confirm_otp_err_occur_msg))
                            .setPositiveListener(requireContext().getString(R.string.confirm_otp)) {
                                it.dismiss()
                            }.build().show()
                    }
                }
            }
        }

        override fun onFailure(call: Call<AuthResponseModel>, t: Throwable) {
            requireActivity().runOnUiThread {
                iOSDialogBuilder(requireContext())
                    .setTitle(requireContext().getString(R.string.request_err))
                    .setSubtitle(requireContext().getString(R.string.err_network_not_connected))
                    .setPositiveListener(requireContext().getString(R.string.confirm_otp)) {
                        it.dismiss()
                    }.build().show()
            }
            Log.e(CheckOTPFragment::class.java.name, t.message ?: "error message")
        }
    }

    private fun getLoginStatus(): Boolean {
        return localDataManager.getUserLoginState()
    }
}