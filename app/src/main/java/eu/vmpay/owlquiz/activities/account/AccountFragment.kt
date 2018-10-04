package eu.vmpay.owlquiz.activities.account

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import eu.vmpay.owlquiz.R
import eu.vmpay.owlquiz.databinding.FragmentAccountBinding
import eu.vmpay.owlquiz.utils.InjectorUtils
import eu.vmpay.owlquiz.utils.InputManagerUtils
import eu.vmpay.owlquiz.utils.SnackbarMessage

class AccountFragment : Fragment() {
    lateinit var viewModel: AccountViewModel
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentAccountBinding>(layoutInflater, R.layout.fragment_account, container, false)
        val factory = InjectorUtils.provideAccountViewModelFactory(requireContext())
        viewModel = ViewModelProviders.of(this, factory)
                .get(AccountViewModel::class.java)
        binding.viewModel = viewModel

        setHasOptionsMenu(true)
        viewModel.snackbarMessage.observe(this, object : SnackbarMessage.SnackbarObserver<Int> {
            override fun onNewMessage(snackbarMessageResourceId: Int) {
                SnackbarMessage.showSnackbar(view, getString(snackbarMessageResourceId))
            }
        })

        val adapter = PlayerListAdapter(viewModel)
        binding.recyclerView.adapter = adapter
        subscribeUi(adapter)

        binding.etPlayerId.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    viewModel.onFabClick()
                    InputManagerUtils.hideKeyboard(activity!!)
                    true
                }
                else -> false
            }
        }

        return binding.root
    }

    private fun subscribeUi(adapter: PlayerListAdapter) {
        viewModel.players.observe(viewLifecycleOwner, Observer { players ->
            if (players != null) adapter.submitList(players)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.menu_accout_activity, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_bookmark -> {
            viewModel.onBookmarkClick()
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
}