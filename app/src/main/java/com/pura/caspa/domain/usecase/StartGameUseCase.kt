package com.pura.caspa.domain.usecase

import com.pura.caspa.data.model.Player
import com.pura.caspa.data.util.PartyError
import com.pura.caspa.data.util.Resource
import com.pura.caspa.domain.repository.PuraCaspaRepository

class StartGameUseCase(private val puraCaspaRepository: PuraCaspaRepository) {
    suspend operator fun invoke(
        roomId: String,
        integrantes: List<Player>,
        usedWords: List<String>
    ): Resource<Unit> {
        //1. We get the words from the repository
        val wordsResult = puraCaspaRepository.getWordstoPlay()
        if (wordsResult !is Resource.Success) {
            return Resource.Error(partyError = PartyError.WORDS_NOT_FOUND)
        }

        //2. Filter: We use the list of words that comes from the repository
        // Assuming wordsResult.data is List<WordModel> and each one has .word
        val availableWords = wordsResult.data!!.words.filter { wordModel ->
            !usedWords.contains(wordModel)
        }

        if (availableWords.isEmpty()) {
            return Resource.Error(PartyError.WITHOUT_WORDS)
        }

        //3. Random
        val selectedWord = availableWords.random()
        val luckyImpostor = integrantes.random()

        //4. Update Firebase
        return puraCaspaRepository.updatePartyStart(
            roomId = roomId,
            word = selectedWord,
            impostor = luckyImpostor.id,
            status = "jugando",
            newUsedWordsList = usedWords + selectedWord //We concatenate the new
        )
    }
}