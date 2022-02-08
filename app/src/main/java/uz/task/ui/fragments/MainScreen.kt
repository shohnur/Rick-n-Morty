package uz.task.ui.fragments

import kotlinx.android.synthetic.main.screen_main.*
import uz.task.R
import uz.task.base.BaseFragment
import uz.task.network.models.AllCharactersResp
import uz.task.ui.adapters.CharacterAdapter

class MainScreen : BaseFragment(R.layout.screen_main) {

    lateinit var adapter: CharacterAdapter

    var page = 1

    override fun initialize() {

        initRecycler()

        showProgress(true)
        viewModel.getCharacters(page++)

    }

    private fun initRecycler() {
        adapter = CharacterAdapter(requireContext())
        recycler.adapter = adapter
        adapter.listener = {
            addFragment(WebViewScreen())
        }

        adapter.loadMoreDataListener = {
            showProgress(true)
            viewModel.getCharacters(page++)
        }
    }

    override fun observe() {
        viewModel.data.observe(viewLifecycleOwner, {
            showProgress(false)
            if (it is AllCharactersResp) {
                adapter.addData(it.results)
            }
        })
    }
}