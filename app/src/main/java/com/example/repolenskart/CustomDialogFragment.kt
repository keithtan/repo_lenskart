package com.example.repolenskart

import android.app.Dialog
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.parcelize.Parcelize

class CustomDialogFragment : DialogFragment() {

    private val args: CustomDialogFragmentArgs by navArgs()
    private lateinit var ownFragmentTag: String

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        ownFragmentTag = args.ownFragmentTag

        return AlertDialog.Builder(requireContext())
            .setTitle(args.dialogTitle)
            .setMessage(args.contentText)
            .setPositiveButton(args.positiveText) { _, _ ->
                returnResult(
                    CustomDialogFragmentResult(
                        dialogTag = ownFragmentTag,
                        code = POSITIVE_PRESSED
                    )
                )
                findNavController().navigateUp()
            }
            .setNegativeButton(args.negativeText) { _, _ ->
                returnResult(
                    CustomDialogFragmentResult(
                        dialogTag = ownFragmentTag,
                        code = NEGATIVE_PRESSED
                    )
                )
            }
            .create()
    }

    private fun returnResult(result: CustomDialogFragmentResult) {
        result.dialogTag?.run {
            setFragmentResult(this, bundleOf(DIALOG_ACTION to result))
        }
    }

    companion object {
        const val DIALOG_ACTION = "DIALOG_ACTION"
        const val POSITIVE_PRESSED = 1
        const val NEGATIVE_PRESSED = 2
    }

}

@Parcelize
data class CustomDialogFragmentResult(
    val dialogTag: String?,
    val code: Int
) : Parcelable