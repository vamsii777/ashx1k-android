package com.dewonderstruck.apps.ashx0.viewmodel.homelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dewonderstruck.apps.ashx0.repository.product.ProductRepository
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.common.PSViewModel
import com.dewonderstruck.apps.ashx0.viewobject.Product
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.holder.ProductParameterHolder
import javax.inject.Inject

class HomeSearchProductViewModel @Inject internal constructor(repository: ProductRepository) : PSViewModel() {
    val getProductListByKeyData: LiveData<Resource<List<Product>>>
    private val getProductListByKeyObj = MutableLiveData<TmpDataHolder>()
    val getNextPageProductListByKeyData: LiveData<Resource<Boolean>>
    private val getNextPageProductListByKeyObj = MutableLiveData<TmpDataHolder>()
    @JvmField
    var holder = ProductParameterHolder().discountParameterHolder
    fun setGetProductListByKeyObj(parameterHolder: ProductParameterHolder, userId: String, limit: String, offset: String) {
        val tmpDataHolder = TmpDataHolder()
        tmpDataHolder.productParameterHolder = parameterHolder
        tmpDataHolder.limit = limit
        tmpDataHolder.offset = offset
        tmpDataHolder.loginUserId = userId
        setLoadingState(true)
        getProductListByKeyObj.value = tmpDataHolder
    }

    fun setGetNextPageProductListByKeyObj(parameterHolder: ProductParameterHolder, userId: String, limit: String, offset: String) {
        val tmpDataHolder = TmpDataHolder()
        tmpDataHolder.productParameterHolder = parameterHolder
        tmpDataHolder.limit = limit
        tmpDataHolder.offset = offset
        tmpDataHolder.loginUserId = userId
        setLoadingState(true)
        getNextPageProductListByKeyObj.value = tmpDataHolder
    }

    internal class TmpDataHolder {
        var productId = ""
        var loginUserId = ""
        var offset = ""
        var catId = ""
        var limit = ""
        var isConnected = false
        var apiKey = ""
        var shopId = ""
        var productParameterHolder = ProductParameterHolder()
    }

    init {
        Utils.psLog("Inside HomeSearchProductViewModel")
        getProductListByKeyData = Transformations.switchMap(getProductListByKeyObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<List<Product>>>()
            }
            repository.getProductListByKey(obj.productParameterHolder, obj.loginUserId, obj.limit, obj.offset)
        }
        getNextPageProductListByKeyData = Transformations.switchMap(getNextPageProductListByKeyObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<Boolean>>()
            }
            repository.getNextPageProductListByKey(obj.productParameterHolder, obj.loginUserId, obj.limit, obj.offset)
        }
    }
}