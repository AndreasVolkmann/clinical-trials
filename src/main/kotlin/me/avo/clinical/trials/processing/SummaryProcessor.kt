package me.avo.clinical.trials.processing

/**
 * Reduce size of summaries by over 50 %
 */
fun List<String>.filterWords(): List<String> =
    filterNot(::isCommon)//.alsoPrint { "Filtered words: ${formatPercent(it.size, this.size)}" }


fun String.filterWords(ignore: List<String>): String = this
    .split(" ") // split into words
    .filter { !isCommon(it) || it in ignore } // only keep words that are in ignore or aren't common
    .joinToString(" ")

val words = setOf<String>(
    "a", "the", "of", "and", "to", "in", "a", "is", "with", "will", "for", "be", "this", "that", "on", "or", "are",
    "as", "by", "an", "at", "have", "after", "from", "their", "has", "can", "which", "group", "use", "been",
    "evaluate", "purpose", "clinical", "study", "patients", "treatment", "investigators", "safety", "randomized",
    "been", "trial", "who", "not", "may", "risk", "determine", "intervention", "efficacy", "care",
    "effects", "patient", "subjects", "two", "during", "using", "these", "also", "s", "dose", "based",
    "it", "if", "one", "three", "used", "effect", "control", "compared", "whether", "between", "assess", "was",
    "non", "aim", "up", "test", "months", "primary", "high", "time", "more", "participants", "weeks", "data",
    "controlled", "studies", "quality", "associated", "standard", "receive", "well", "objective", "research",
    "improve", "other", "function", "outcomes", "years", "compare", "than", "first",
    "effective", "single", "related", "were", "such", "rate", "response", "different",
    "investigate", "no", "levels", "new", "over", "symptoms", "effectiveness", "post", "but", "how", "stop",
    "combination", "treating", "phase", "day", "we", "following", "when", "given", "they", "about",
    "days", "either", "help", "without", "term", "long", "daily", "treated", "rationale",
    "low", "into", "type", "system", "doses", "week", "side", "ways", "healthy", "each", "both", "them",
    "before", "acute", "administered", "tests", "designed", "people", "stage", "advanced", "pressure",
    "changes", "there", "known", "normal", "hypothesis", "studying", "reduce", "tolerability", "information",
    "most", "groups", "results", "outcome", "including", "all", "however", "follow", "its", "open", "increase",
    "prospective", "impact", "factors", "performed", "early", "program", "among", "specific", "through",
    "aims", "management", "provide", "d", "t", "could", "only", "better", "common", "level",
    "shown", "severe", "potential", "total", "current", "increased", "year", "those", "double", "evidence",
    "undergoing", "pilot", "development", "addition", "administration", "b", "goal", "versus", "measured",
    "label", "important", "functional", "being", "e", "individuals", "within", "project", "device",
    "include", "number", "assessment", "complications", "main", "analysis", "due", "cause", "diagnosis",
    "c", "would", "procedure", "assessed", "present", "baseline", "feasibility", "examine", "conducted",
    "any", "self", "order", "identify", "some", "part", "out", "significant", "evaluated", "followed",
    "small", "method", "measure", "loss", "failure", "multiple", "measures", "technique", "lower",
    "under", "major", "change", "support", "practice", "evaluation", "treatments", "incidence", "our",
    "design", "center", "secondary", "approach", "positive", "hours", "protocol", "goals", "cover", "wide",
    "range", "success", "before", "background", "increases", "develop", "interventions", "parameters",
    "need", "month", "pre", "end", "called", "receiving", "reduction", "overall", "prior", "less", "reported",
    "evaluating", "available", "objectives", "explore", "before"

)

val ignore = setOf(
    "cancer", "surgery", "blood", "pain", "therapy", "children", "women", "cells", "chronic", "tumor",
    "chemotherapy", "cell", "growth", "drug", "heart", "body", "HIV", "brain", "immune", "age", "breast",
    "vaccine", "diabetes", "bone", "radiation", "oral", "period", "kill", "drugs", "lung", "insulin",
    "infection", "physical", "surgical", "exercise", "training", "medical", "cognitive", "tissue",
    "adults", "muscle", "weight", "imaging", "arm", "hospital", "blind", "cardiac", "anti", "postoperative",
    "survival", "liver", "stroke", "glucose", "sleep", "MRI", "life", "activity", "cardiovascular", "human",
    "therapeutic", "medication", "mortality", "skin", "invasive", "inflammatory"
)

fun isCommon(text: String) = text.toLowerCase() in words

/*
How to debug a specific falsely predicted LOV.
Identify common words that are essential for this LOV to be identified and differentiated from others.

It's possible that some LOVs need words that are filtered

 */