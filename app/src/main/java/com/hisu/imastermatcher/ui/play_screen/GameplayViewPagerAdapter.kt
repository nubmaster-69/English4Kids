package com.hisu.imastermatcher.ui.play_screen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hisu.imastermatcher.ui.play_style.audio_image.MatchingAudioImagePairsFragment
import com.hisu.imastermatcher.ui.play_style.class_pairs_matching.ClassicPairsMatchingFragment
import com.hisu.imastermatcher.ui.play_style.complete_sentence.SentenceStyleFragment
import com.hisu.imastermatcher.ui.play_style.match_pairs.MatchingWordPairsFragment
import com.hisu.imastermatcher.ui.play_style.type_answer.TypeAnswerFragment

class GameplayViewPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val itemTapListener: () -> Unit,
    private val wrongAnswerListener: () -> Unit
) : FragmentStateAdapter(fragmentActivity) {

    var gameplays = listOf<String>()

    override fun getItemCount(): Int = gameplays.size

    override fun createFragment(position: Int): Fragment {
        //TODO: return gameplay for each gameplay style
        val curGameplay = gameplays[position]

        if (curGameplay == "sentence") {
            return SentenceStyleFragment(itemTapListener, wrongAnswerListener)
        }

        if (curGameplay == "word_pair") {
            return MatchingWordPairsFragment(itemTapListener, wrongAnswerListener)
        }

        if (curGameplay == "audio_image_pair") {
            return MatchingAudioImagePairsFragment(itemTapListener, wrongAnswerListener)
        }

        if(curGameplay == "classic_pairs") {
            return ClassicPairsMatchingFragment(itemTapListener, wrongAnswerListener)
        }

        return TypeAnswerFragment(itemTapListener, wrongAnswerListener)
    }
}