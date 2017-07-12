package me.avo.clinical.trials.jet

import com.hazelcast.jet.Jet
import com.hazelcast.jet.function.DistributedFunction

object HazelJet {


    fun run() {
        val jet = Jet.newJetInstance()
        Jet.newJetInstance()

        val source = jet.getMap<Int, String>("source")

        source.put(0, "It was the best of times, " +
                "it was the worst of times ")
        source.put(1, "There were a king with a large jaw and a " +
                "queen with a plain face, on the that things in " +
                "general were settled for ever.")
        source.put(2, "It was the year of Our Lord one thousand " +
                "seven hundred and seventy-five. Spiritual " +
                "revelations were conceded to England at that " +
                "favoured period")

        val counts = source.stream()
                .map<String>(DistributedFunction<MutableMap.MutableEntry<Int, String>, String> ({ it.value.toUpperCase()}))



    }


}