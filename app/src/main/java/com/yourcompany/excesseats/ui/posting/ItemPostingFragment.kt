package com.yourcompany.excesseats.ui.posting

import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.yourcompany.excesseats.R
import com.yourcompany.excesseats.data.model.Item
import com.yourcompany.excesseats.data.model.FoodType
import com.yourcompany.excesseats.data.repository.ItemRepository
import com.yourcompany.excesseats.databinding.FragmentItemPostingBinding
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.UUID

class ItemPostingFragment : Fragment() {
    private var _binding: FragmentItemPostingBinding? = null
    private val binding get() = _binding!!
    private val itemRepository = ItemRepository.getInstance()
    private var selectedImageUri: Uri? = null

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            binding.imagePreview.setImageURI(it)
            binding.imagePreview.visibility = View.VISIBLE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemPostingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupFoodTypeDropdown()
        setupTimePickerDialog()
        setupImageUpload()
        setupSubmitButton()
    }

    private fun setupFoodTypeDropdown() {
        val foodTypes = resources.getStringArray(R.array.food_types)
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, foodTypes)
        binding.foodTypeInput.setAdapter(adapter)
    }

    private fun setupTimePickerDialog() {
        binding.pickupTimeInput.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            TimePickerDialog(
                requireContext(),
                { _, selectedHour, selectedMinute ->
                    binding.pickupTimeInput.setText(String.format("%02d:%02d", selectedHour, selectedMinute))
                },
                hour,
                minute,
                true
            ).show()
        }
    }

    private fun setupImageUpload() {
        binding.uploadImageButton.setOnClickListener {
            getContent.launch("image/*")
        }
    }

    private fun setupSubmitButton() {
        binding.submitButton.setOnClickListener {
            val title = binding.titleInput.text.toString()
            val foodType = binding.foodTypeInput.text.toString()
            val quantity = binding.quantityInput.text.toString()
            val location = binding.locationInput.text.toString()
            val pickupTime = binding.pickupTimeInput.text.toString()
            val description = binding.descriptionInput.text.toString()
            val containersAvailable = binding.containerSwitch.isChecked

            if (validateInput(title, foodType, quantity, location, pickupTime)) {
                lifecycleScope.launch {
                    val item = Item(
                        id = UUID.randomUUID().toString(),
                        title = title,
                        foodType = getFoodType(foodType),
                        quantity = quantity,
                        location = location,
                        pickupTimeStart = convertTimeToMillis(pickupTime),
                        description = description,
                        containerAvailable = containersAvailable,
                        imageUrl = selectedImageUri?.toString()
                    )

                    val result = itemRepository.createItem(item)
                    if (result.isSuccess) {
                        Toast.makeText(context, "Food posted successfully!", Toast.LENGTH_SHORT).show()
                        clearForm()
                    } else {
                        Toast.makeText(context, "Failed to post food", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun validateInput(
        title: String,
        foodType: String,
        quantity: String,
        location: String,
        pickupTime: String
    ): Boolean {
        when {
            title.isEmpty() -> {
                Toast.makeText(context, "Please enter a title", Toast.LENGTH_SHORT).show()
                return false
            }
            foodType.isEmpty() -> {
                Toast.makeText(context, "Please select a food type", Toast.LENGTH_SHORT).show()
                return false
            }
            quantity.isEmpty() -> {
                Toast.makeText(context, "Please enter quantity", Toast.LENGTH_SHORT).show()
                return false
            }
            location.isEmpty() -> {
                Toast.makeText(context, "Please enter location", Toast.LENGTH_SHORT).show()
                return false
            }
            pickupTime.isEmpty() -> {
                Toast.makeText(context, "Please select pickup time", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }

    private fun getFoodType(foodType: String): FoodType {
        return when (foodType.uppercase()) {
            "SANDWICHES" -> FoodType.PREPARED_MEAL
            "PIZZA" -> FoodType.PREPARED_MEAL
            "SALAD" -> FoodType.PRODUCE
            "DESSERTS" -> FoodType.BAKED_GOODS
            else -> FoodType.OTHER
        }
    }

    private fun convertTimeToMillis(time: String): Long {
        val parts = time.split(":")
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, parts[0].toInt())
        calendar.set(Calendar.MINUTE, parts[1].toInt())
        return calendar.timeInMillis
    }

    private fun clearForm() {
        binding.titleInput.text?.clear()
        binding.foodTypeInput.text?.clear()
        binding.quantityInput.text?.clear()
        binding.locationInput.text?.clear()
        binding.pickupTimeInput.text?.clear()
        binding.descriptionInput.text?.clear()
        binding.containerSwitch.isChecked = false
        binding.imagePreview.setImageURI(null)
        binding.imagePreview.visibility = View.GONE
        selectedImageUri = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
