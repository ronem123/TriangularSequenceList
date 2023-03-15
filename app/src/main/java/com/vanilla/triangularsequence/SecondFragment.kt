package com.vanilla.triangularsequence

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.vanilla.triangularsequence.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private val navArgs: SecondFragmentArgs by navArgs()
    private var _binding: FragmentSecondBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateData(navArgs.tsValue)
    }

    private fun updateData(n: Int) {
        val listItem = getListItems(n)

        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = TriangularSequenceListAdapter(listItem)
        }
    }

    private fun getListItems(n: Int): ArrayList<ListData> {
        val tsList = getTriangularSequence(n)
        val list = ArrayList<ListData>()

        (1..tsList.last()).forEach { n ->
            var isTriangleSequenceElement = false
            tsList.forEach { ts ->
                if (n == ts) isTriangleSequenceElement = true
            }
            list.add(
                ListData(
                    icon = if (isTriangleSequenceElement) navArgs.dogUri else navArgs.catUri,
                    index = if (isTriangleSequenceElement) "$n Dog" else "$n Cat",
                )
            )
        }
        return list
    }

    private fun getTriangularSequence(n: Int): ArrayList<Int> {
        var j = 1
        var k = 1
        var i = 1

        val list = ArrayList<Int>()
        while (i <= n) {
            list.add(k)
            j += 1
            k += j
            i++
        }
        return list
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

data class ListData(
    val icon: String,
    val index: String
)