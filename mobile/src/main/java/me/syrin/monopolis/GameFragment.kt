package me.syrin.monopolis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_game.*
import me.syrin.monopolis.common.BoardFragment
import me.syrin.monopolis.common.GameButtonsFragment
import me.syrin.monopolis.common.game.Monopolis
import me.syrin.monopolis.common.network.NetworkHandler

class GameFragment : Fragment() {
    lateinit var monopolis: Monopolis

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        monopolis = Monopolis(activity!!, NetworkHandler.lobby.value!!.players)


        (childFragmentManager.findFragmentById(R.id.fragment_board) as BoardFragment).initialiseBoard(monopolis)
        (childFragmentManager.findFragmentById(R.id.fragment_buttons) as GameButtonsFragment).game = monopolis

        monopolis.uiUpdates.observe(this, Observer {
            updateUi()
        })

        monopolis.start()
    }

    private fun updateUi() {
        // TODO: update UI
        (childFragmentManager.findFragmentById(R.id.fragment_buttons) as GameButtonsFragment).uiUpdate()
    }
}
